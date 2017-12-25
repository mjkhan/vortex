package vortex.application;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import egovframework.rte.fdl.property.EgovPropertyService;
import vortex.application.code.service.CodeMapper;
import vortex.support.data.DataObject;
import vortex.support.database.AbstractService;

public class ApplicationService extends AbstractService {
	@Resource(name="sqlSession")
	private SqlSessionFactory sqlSessionFactory;
	@Autowired
	protected EgovPropertyService properties;
	
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
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
}