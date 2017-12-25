package vortex.application.access.service;

import vortex.support.data.DataObject;

public interface RoleService {
	public DataObject getRoles(DataObject req);
	
	public DataObject getRolesFor(DataObject req);
	
	public DataObject getRole(DataObject req);
	
	public DataObject create(DataObject req);
	
	public DataObject update(DataObject req);
	
	public DataObject delete(DataObject req);
	
	public DataObject getActions(DataObject req);
	
	public DataObject addActions(DataObject req);
	
	public DataObject deleteActions(DataObject req);
	
	public DataObject getUsers(DataObject req);
	
	public DataObject addUsers(DataObject req);
	
	public DataObject deleteUsers(DataObject req);
}