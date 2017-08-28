package vortex.application;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import vortex.application.code.service.CodeMapper;
import vortex.support.data.DataObject;
import vortex.support.database.AbstractService;

public class ApplicationService extends AbstractService implements ApplicationAccess {
	@Resource(name="sqlSession")
	private SqlSessionFactory sqlSessionFactory;
	
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Autowired
	protected CodeMapper codeMapper;
	
	@Override
	public DataObject access(DataObject req) {
		return null;
	}
	
	protected DataObject dataobject() {
		return new DataObject();
	}
}