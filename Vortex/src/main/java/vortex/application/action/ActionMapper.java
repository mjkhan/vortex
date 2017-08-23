package vortex.application.action;

import java.util.List;

import vortex.support.data.DataObject;
import vortex.support.database.AbstractMapper;

public class ActionMapper extends AbstractMapper {
	public List<DataObject> getActions(String groupID) {
		return selectList("action.getActions", groupID);
	}
	
	public Action getAction(String actionID) {
		return selectOne("action.getAction", actionID);
	}
	
	public int create(Action action) {
		return insert("action.insert", action);
	}
	
	public int update(Action action) {
		return update("action.update", action);
	}
	
	public int deleteActions(String groupID, String... actionIDs) {
		return delete(
			"action.delete"
		   , params().set("groupID", groupID)
		  			 .set("actionIDs", !isEmpty(actionIDs) ? actionIDs : null)
		);
	}
}