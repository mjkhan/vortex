package vortex.application.access.service;

import vortex.support.database.AbstractMapper;

public class RoleActionMapper extends AbstractMapper {	
	public int add(String addedBy, String[] roleIDs, String... actionIDs) {
		if (isEmpty(roleIDs) || isEmpty(actionIDs)) return 0;
		
		return insert(
			"roleAction.insert"
		   , params().set("roleIDs", roleIDs)
		   			 .set("actionIDs", actionIDs)
		   			 .set("addedBy", addedBy)
		);
	}
	
	public int delete(String[] roleIDs, String... actionIDs) {
		return delete(
			"roleAction.delete"
		   , params().set("roleIDs", !isEmpty(roleIDs) ? roleIDs : null)
		   			 .set("actionIDs", !isEmpty(actionIDs) ? actionIDs : null)
		);
	}
}