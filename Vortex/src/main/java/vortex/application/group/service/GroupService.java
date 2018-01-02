package vortex.application.group.service;

import vortex.application.group.Group;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

public interface GroupService {
	public BoundedList<DataObject> searchGroups(DataObject req);
	
	public DataObject getInfo(String groupID);
	
	public Group getGroup(String groupID);
	
	public boolean create(Group group);
	
	public boolean update(Group group);
	
	public boolean remove(String... groupIDs);
	
	public boolean delete(String... groupIDs);
}