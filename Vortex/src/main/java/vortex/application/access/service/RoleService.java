package vortex.application.access.service;

import vortex.application.group.Group;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

public interface RoleService {
	public BoundedList<DataObject> search(DataObject req);

	public DataObject getInfo(String roleID);
	
	public Group getRole(String roleID);
	
	public boolean create(Group group);
	
	public boolean update(Group group);

	public int delete(String... roleIDs);
	
	public BoundedList<DataObject> getUsers(DataObject req);
	
	public int addUsers(String[] roleIDs, String[] userIDs);
	
	public int deleteUsers(String[] roleIDs, String[] userIDs);
	
	public BoundedList<DataObject> getPermissions(DataObject req);
	
	public int addPermissions(String[] roleIDs, String[] permissionIDs);
	
	public int deletePermissions(String[] roleIDs, String[] permissionIDs);
}