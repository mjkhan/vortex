package vortex.application;

import java.util.Map;

import org.springframework.security.access.AccessDeniedException;

import vortex.support.data.DataObject;
import vortex.support.database.AbstractMapper;

public class DataMapper extends AbstractMapper {
	protected User currentUser() {
		return User.current();
	}
	
	protected Access currentAccess() {
		return Access.current();
	}
	
	@Override
	protected DataObject params() {
		return params(false);
	}
	
	protected void setCurrent(Map<String, Object> map) {
		map.put("currentUser", currentUser());
		map.put("currentAccess", currentAccess());
	}
	
	protected DataObject params(boolean current) {
		DataObject p = super.params();
		if (current) {
			setCurrent(p);
		}
		return p;
	}
	
	protected AccessDeniedException denyAccess(String msg) {
		return new AccessDeniedException(msg);
	}
	
	protected static ApplicationException exception(Throwable cause) {
		return ApplicationException.create(cause);
	}
}