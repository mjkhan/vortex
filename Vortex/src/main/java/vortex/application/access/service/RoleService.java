package vortex.application.access.service;

import vortex.application.ApplicationAccess;
import vortex.support.data.DataObject;

public interface RoleService extends ApplicationAccess {
	public DataObject getRoles(DataObject req);
	
	public DataObject getRole(DataObject req);
	
	public DataObject create(DataObject req);
	
	public DataObject update(DataObject req);
	
	public DataObject delete(DataObject req);
}