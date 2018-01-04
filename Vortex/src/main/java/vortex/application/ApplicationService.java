package vortex.application;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import egovframework.rte.fdl.property.EgovPropertyService;
import vortex.application.code.service.CodeMapper;
import vortex.support.data.DataObject;
import vortex.support.database.AbstractService;

public class ApplicationService extends AbstractService {
	protected static EgovPropertyService properties;

	@Resource(name="sqlSession")
	private SqlSessionFactory sqlSessionFactory;
	
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Autowired
	public void setProperties(EgovPropertyService props) {
		properties = props;
	}
	
	@Autowired
	protected CodeMapper codeMapper;
	
	protected DataObject dataobject() {
		return new DataObject();
	}
	
	protected User currentUser() {
		return User.current();
	}
	
	protected Client client() {
		return Client.current();
	}
	
	protected AccessDeniedException denyAccess(String msg) {
		return new AccessDeniedException(msg);
	}
}