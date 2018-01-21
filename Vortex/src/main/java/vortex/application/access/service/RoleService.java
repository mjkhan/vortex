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
	
	public DataObject getUsers(DataObject req);
	
	public DataObject addUsers(DataObject req);
	
	public DataObject deleteUsers(DataObject req);
	
	public DataObject getActions(DataObject req);
	
	public DataObject addActions(DataObject req);
	
	public DataObject deleteActions(DataObject req);
}