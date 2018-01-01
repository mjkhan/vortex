package vortex.application.access.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import vortex.application.DataMapper;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Repository("roleMemberMapper")
public class RoleMemberMapper extends DataMapper {	
	public BoundedList<Map<String, Object>> getActions(DataObject req) {
		List<Map<String, Object>> list = selectList("roleMember.getActions", req);
		return boundedList(list, req);
	}

	public int addActions(String[] roleIDs, String... actionIDs) {
		if (isEmpty(roleIDs) || isEmpty(actionIDs)) return 0;
		
		return insert(
			"roleMember.addActions"
		   , params(true)
		   	.set("roleIDs", roleIDs)
		   	.set("actionIDs", actionIDs)
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

	public int addUsers(String[] roleIDs, String... userIDs) {
		if (isEmpty(roleIDs) || isEmpty(userIDs)) return 0;
		
		return insert(
			"roleMember.addUsers"
		   , params(true)
		   	.set("roleIDs", roleIDs)
		   	.set("userIDs", userIDs)
		);
	}
	
	public int deleteUsers(String[] roleIDs, String... userIDs) {
		return delete(
			"roleMember.deleteUsers"
		   , params().set("roleIDs", !isEmpty(roleIDs) ? roleIDs : null)
		   			 .set("userIDs", !isEmpty(userIDs) ? userIDs : null)
		);
	}
	
	public List<Role> getRoles(String userID) {
		return selectList("role.getUserRoles", userID);
	}
	
	public boolean isPermitted(String userID, String actionPath) {
		int count = selectOne(
			"roleMember.countUserRolesForAction"
		  , params().set("userID", userID)
					.set("actionPath", actionPath)
		);
		return count > 0;
	}
}