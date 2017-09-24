package vortex.application.access.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import vortex.support.database.AbstractMapper;

@Repository("roleMemberMapper")
public class RoleMemberMapper extends AbstractMapper {	
	public List<Map<String, Object>> getActions(String roleID) {
		return selectList("roleMember.getActions", roleID);
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

	public List<Map<String, Object>> getUsers(String roleID) {
		return selectList("roleMember.getUsers", roleID);
	}

	public int addUsers(String addedBy, String[] roleIDs, String userType, String... userIDs) {
		if (isEmpty(roleIDs) || isEmpty(userIDs)) return 0;
		
		return insert(
			"roleMember.addUsers"
		   , params().set("roleIDs", roleIDs)
		   			 .set("userType", userType)
		   			 .set("userIDs", userIDs)
		   			 .set("addedBy", addedBy)
		);
	}
	
	public int deleteUsers(String[] roleIDs, String userType, String... userIDs) {
		return delete(
			"roleMember.deleteUsers"
		   , params().set("roleIDs", !isEmpty(roleIDs) ? roleIDs : null)
 			 		 .set("userType", userType)
		   			 .set("userIDs", !isEmpty(userIDs) ? userIDs : null)
		);
	}
}