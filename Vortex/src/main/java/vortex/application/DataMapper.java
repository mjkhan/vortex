package vortex.application;

import vortex.support.data.DataObject;
import vortex.support.database.AbstractMapper;

public class DataMapper extends AbstractMapper {
	protected User currentUser() {
		return User.current();
	}
	
	@Override
	protected DataObject params() {
		return params(true);
	}
	
	protected DataObject params(boolean currentUser) {
		DataObject p = super.params();
		if (currentUser)
			p.put("currentUser", currentUser());
		return p;
	}
}