package vortex.support.database;

import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import vortex.support.AbstractObject;
import vortex.support.data.DataObject;

public class GenericService extends AbstractObject {
	protected void setFailed(DataObject res, Throwable e) {
		Throwable t = rootCause(e);
		res.set("failed", Boolean.TRUE)
		   .set("error", t);
	}
	
	protected void read(String sessionName, SqlSessionFactory sessionFactory, Consumer<SqlSession> task) {
		if (task == null) return;

		SqlSession sqlSession = notEmpty(sessionFactory, "sessionFactory").openSession(false);
		SqlSessionDispenser.set(sessionName, sqlSession);
		log().debug(() -> sessionName + " opened.");

		try {
			task.accept(sqlSession);
		} finally {
			sqlSession.close();
			SqlSessionDispenser.remove(sessionName);
			log().debug(() -> sessionName + " closed.");
		}
	}
	
	protected <T> T read(String sessionName, SqlSessionFactory sessionFactory, Function<SqlSession, T> task) {
		if (task == null) return null;

		SqlSession sqlSession = notEmpty(sessionFactory, "sessionFactory").openSession(false);
		SqlSessionDispenser.set(sessionName, sqlSession);
		log().debug(() -> sessionName + " opened.");
		try {
			return task.apply(sqlSession);
		} finally {
			sqlSession.close();
			SqlSessionDispenser.remove(sessionName);
			log().debug(() -> sessionName + " closed.");
		}
	}

	protected TransactionDefinition txDef() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		return def;
	}
	
	protected void write(String sessionName, SqlSessionFactory sessionFactory, DataSourceTransactionManager txm, Consumer<SqlSession> task) {
		if (task == null) return;

		SqlSession sqlSession = notEmpty(sessionFactory, "sessionFactory").openSession(false);
		SqlSessionDispenser.set(sessionName, sqlSession);
		log().debug(() -> sessionName + " opened.");
		TransactionStatus status = txm.getTransaction(txDef());
		try {
			task.accept(sqlSession);
			txm.commit(status);
			log().debug(() -> "Transaction committed.");
		} catch (Exception e) {
			txm.rollback(status);
			log().debug(() -> "Transaction rolled back");
			throw e;
		} finally {
			sqlSession.close();
			SqlSessionDispenser.remove(sessionName);
			log().debug(() -> sessionName + " closed.");
		}
	}
	
	protected <T> T write(String sessionName, SqlSessionFactory sessionFactory, DataSourceTransactionManager txm, Function<SqlSession, T> task) {
		if (task == null) return null;

		SqlSession sqlSession = notEmpty(sessionFactory, "sessionFactory").openSession(false);
		SqlSessionDispenser.set(sessionName, sqlSession);
		log().debug(() -> sessionName + " opened.");
		TransactionStatus status = txm.getTransaction(txDef());
		try {
			T t = task.apply(sqlSession);
			txm.commit(status);
			log().debug(() -> "Transaction committed.");
			return t;
		} catch (Exception e) {
			txm.rollback(status);
			log().debug(() -> "Transaction rolled back");
			throw e;
		} finally {
			sqlSession.close();
			SqlSessionDispenser.remove(sessionName);
			log().debug(() -> sessionName + " closed.");
		}
	}
}