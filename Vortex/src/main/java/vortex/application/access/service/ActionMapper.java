package vortex.application.access.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import vortex.application.DataMapper;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

@Repository("actionMapper")
public class ActionMapper extends DataMapper {
	public List<DataObject> getActions(String groupID) {
		return selectList("action.getActions", groupID);
	}
	
	public DataObject getInfo(String actionID) {
		return selectOne("action.getInfo", actionID);
	}
	
	public Action getAction(String actionID) {
		return selectOne("action.getAction", actionID);
	}
	
	public Action findAction(String actionPath) {
		return selectOne("action.findAction", actionPath);
	}

	public boolean create(Action action) {
		return action != null ? insert("action.insert", params(true).set("action", action)) == 1 : false;
	}
	
	public boolean update(Action action) {
		return action != null ? update("action.update", params(true).set("action", action)) == 1: false;
	}
	
	public int setStatus(String status, String groupID, String... actionIDs) {
		return update(
			"action.setStatus"
		   , params(true)
		   		.set("status", status)
		   		.set("groupID", groupID)
		  		.set("actionIDs", !isEmpty(actionIDs) ? actionIDs : null)
		);
	}
	
	public int remove(String groupID, String... actionIDs) {
		return setStatus(Status.REMOVED.code(), groupID, actionIDs);
	}
	
	public int deleteActions(String groupID, String... actionIDs) {
		return delete(
			"action.delete"
		   , params(true)
		   		.set("groupID", groupID)
		  		.set("actionIDs", !isEmpty(actionIDs) ? actionIDs : null)
		);
	}
}