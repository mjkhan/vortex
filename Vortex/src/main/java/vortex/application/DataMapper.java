package vortex.application;

import org.springframework.security.access.AccessDeniedException;

import vortex.support.data.DataObject;
import vortex.support.database.AbstractMapper;

public class DataMapper extends AbstractMapper {
	protected User currentUser() {
		return User.current();
	}
	
	protected Client currentClient() {
		return Client.current();
	}
	
	@Override
	protected DataObject params() {
		return params(false);
	}
	
	protected DataObject params(boolean currentUser) {
		DataObject p = super.params();
		if (currentUser) {
			p.set("currentUser", currentUser())
			 .set("currentClient", currentClient());
		}
		return p;
	}
	
	protected AccessDeniedException denyAccess(String msg) {
		return new AccessDeniedException(msg);
	}
}