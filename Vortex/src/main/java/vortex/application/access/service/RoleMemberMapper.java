package vortex.application.access.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;
import vortex.support.database.AbstractMapper;

@Repository("roleMemberMapper")
public class RoleMemberMapper extends AbstractMapper {	
	public BoundedList<Map<String, Object>> getActions(DataObject req) {
		List<Map<String, Object>> list = selectList("roleMember.getActions", req);
		return boundedList(list, req);
	}
	
	public int addActions(String addedBy, String[] roleIDs, String... actionIDs) {
		if (isEmpty(roleIDs) || isEmpty(actionIDs)) return 0;
		
		return insert(
			"roleMember.addActions"
		   , params().set("roleIDs", roleIDs)
		   			 .set("actionIDs", actionIDs)
		   			 .set("addedBy", addedBy)
		);
	}
	
	public int deleteActions(String[] roleIDs, String... actionIDs) {
		return delete(
			"roleMember.deleteActions"
		   , params().set("roleIDs", !isEmpty(roleIDs) ? roleIDs : null)
		   			 .set("actionIDs", !isEmpty(actionIDs) ? actionIDs : null)
		);
	}
	
	public int deleteActionsOfGroups(String... groupIDs) {
		return delete(
			"roleMember.deleteActionsOfGroups"
		   , params().set("groupIDs", groupIDs)
		);
	}

	public BoundedList<Map<String, Object>> getUsers(DataObject req) {
		List<Map<String, Object>> list = selectList("roleMember.getUsers", req);
		return boundedList(list, req);
	}

	public int addUsers(String addedBy, String[] roleIDs, String... userIDs) {
		if (isEmpty(roleIDs) || isEmpty(userIDs)) return 0;
		
		return insert(
			"roleMember.addUsers"
		   , params().set("roleIDs", roleIDs)
		   			 .set("userIDs", userIDs)
		   			 .set("addedBy", addedBy)
		);
	}
	
	public int deleteUsers(String[] roleIDs, String... userIDs) {
		return delete(
			"roleMember.deleteUsers"
		   , params().set("roleIDs", !isEmpty(roleIDs) ? roleIDs : null)
		   			 .set("userIDs", !isEmpty(userIDs) ? userIDs : null)
		);
	}
}