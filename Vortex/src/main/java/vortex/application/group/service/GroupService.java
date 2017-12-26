package vortex.application.group.service;

import vortex.application.group.Group;
import vortex.support.data.DataObject;

public interface GroupService {
	public DataObject getGroups(DataObject req);
	
	public DataObject getInfo(String groupID);
	
	public Group getGroup(String groupID);
	
	public Group create(Group group);
	
	public boolean update(Group group);
	
	public boolean removeGroups(String... groupIDs);
	
	public boolean deleteGroups(String... groupIDs);
}