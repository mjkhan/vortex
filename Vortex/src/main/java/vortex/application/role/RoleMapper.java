package vortex.application.role;

import java.util.List;

import vortex.support.data.DataObject;
import vortex.support.database.AbstractMapper;

public class RoleMapper extends AbstractMapper {
	public List<DataObject> getRoles(String groupID) {
		return selectList("role.getRoles", groupID);
	}
	
	public Role getRole(String roleID) {
		return selectOne("role.getRole", roleID);
	}
	
	private String newID() {
		return selectOne("role.newID");
	}
	
	public String create(Role role) {
		role.setId(newID());
		insert("role.insert", role);
		return role.getId();
	}
	
	public int update(Role role) {
		return update("role.update", role);
	}
	
	public int deleteRoles(String... roleIDs) {
		return delete(
			"role.delete"
		   , params().set("roleIDs", !isEmpty(roleIDs) ? roleIDs : null)
		);
	}
	
	public int addActions(String addedBy, String[] roleIDs, String... actionIDs) {
		if (isEmpty(roleIDs) || isEmpty(actionIDs)) return 0;
		
		return delete(
			"role.addActions"
		   , params().set("roleIDs", roleIDs)
		   			 .set("actionIDs", actionIDs)
		   			 .set("addedBy", addedBy)
		);
	}
	
	public int deleteActions(String[] roleIDs, String... actionIDs) {
		if (isEmpty(roleIDs)) return 0;
		
		return delete(
			"role.deleteActions"
		   , params().set("roleIDs", roleIDs)
		   			 .set("actionIDs", !isEmpty(actionIDs) ? actionIDs : null)
		);
	}
}