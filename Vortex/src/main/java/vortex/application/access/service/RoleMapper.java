package vortex.application.access.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import vortex.support.data.DataObject;
import vortex.support.database.AbstractMapper;

@Repository("roleMapper")
public class RoleMapper extends AbstractMapper {
	public List<DataObject> getRoles() {
		return selectList("role.getRoles");
	}
	
	public List<DataObject> getRolesFor(String member) {
		return selectList("role.getRolesForMember", params().set("member", member));
	}
	
	public Role getRole(String roleID) {
		return selectOne("role.getRole", roleID);
	}

	public String create(Role role) {
		insert("role.insert", role);
		return role.getId();
	}
	
	public int update(Role role) {
		return update("role.update", role);
	}
	
	public int remove(String... roleIDs) {
		return delete(
			"role.remove"
		   , params().set("roleIDs", !isEmpty(roleIDs) ? roleIDs : null)
		);
	}
}