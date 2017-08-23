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
	
	public int deleteRoles(String groupID, String... roleIDs) {
		return delete(
			"role.delete"
		   , params().set("groupID", groupID)
		  			 .set("roleIDs", !isEmpty(roleIDs) ? roleIDs : null)
		);
	}
}