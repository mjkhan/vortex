package vortex.application.access.service;

import java.util.List;

import vortex.application.group.Group;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

public interface ActionService {
	public List<DataObject> getGroups(DataObject req);
	
	public DataObject getGroupInfo(String groupID);
	
	public Group getGroup(String groupID);
	
	public boolean create(Group group);
	
	public boolean update(Group group);
	
	public int setGroupStatus(String status, String... groupIDs);
	
	public default int removeGroups(String... groupIDs) {
		return setGroupStatus(Status.REMOVED.code(), groupIDs);
	}
	
	public int deleteGroups(String... groupIDs);
	
	public List<DataObject> getActions(String groupID);
	
	public DataObject getInfo(String actionID);
	
	public Action getAction(String actionID);
	
	public boolean create(Action action);
	
	public boolean update(Action action);
	
	public int setStatus(String status, String... actionIDs);
	
	public default int removeActions(String... actionIDs) {
		return setStatus(Status.REMOVED.code(), actionIDs);
	}
	
	public int deleteActions(String groupID, String... actionIDs);
	
	public Action.Permission getPermission(String userID, String actionPath);
}