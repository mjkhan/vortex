package vortex.application.access.service;

import vortex.support.data.DataObject;

public interface RoleService {
	public DataObject getRoles(DataObject req);
	
	public DataObject getRolesFor(DataObject req);
	
	public DataObject getInfo(String roleID);
	
	public Role getRole(String roleID);
	
	public boolean create(Role role);
	
	public boolean update(Role role);
	
	public int delete(String... roleIDs);
	
	public DataObject getActions(DataObject req);
	
	public DataObject addActions(DataObject req);
	
	public DataObject deleteActions(DataObject req);
	
	public DataObject getUsers(DataObject req);
	
	public DataObject addUsers(DataObject req);
	
	public DataObject deleteUsers(DataObject req);
}