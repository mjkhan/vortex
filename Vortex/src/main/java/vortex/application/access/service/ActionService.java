package vortex.application.access.service;

import java.util.List;

public interface ActionService {
	public List<String> getPrefixes();
	
	public List<String> getActions(String prefix);
	
	public int changeAction(String oldName, String newName);
	
	public Permission.Status getPermission(String userID, String actionPath);
	
/*	
	public List<DataObject> getGroups(DataObject req);

	public DataObject getGroupInfo(String groupID);
	
	public Group getGroup(String groupID);
	
	public boolean create(Group group);
	
	public boolean update(Group group);
	
	public int deleteGroups(String... groupIDs);
	
	public List<DataObject> getActions(String groupID);
	
	public DataObject getInfo(String actionID);
	
	public Action getAction(String actionID);
	
	public boolean create(Action action);
	
	public boolean update(Action action);
	
	public int delete(String... actionIDs);
*/
}