package vortex.application.access.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import vortex.application.DataMapper;
import vortex.support.data.DataObject;

@Repository("roleMapper")
public class RoleMapper extends DataMapper {
	public List<DataObject> getRoles() {
		return selectList("role.getRoles");
	}
	
	public List<DataObject> getRolesFor(String member) {
		return selectList("role.getRolesForMember", params().set("member", member));
	}
	
	public DataObject getInfo(String roleID) {
		return selectOne("role.getInfo", roleID);
	}
	
	public Role getRole(String roleID) {
		return selectOne("role.getRole", roleID);
	}

	public boolean create(Role role) {
		return role != null ? insert("role.insert", params(true).set("role", role)) == 1 : false;
	}
	
	public boolean update(Role role) {
		return role != null ? update("role.update", params(true).set("role", role)) == 1 : false;
	}
	
	public int remove(String... roleIDs) {
		return delete(
			"role.remove"
		   , params().set("roleIDs", !isEmpty(roleIDs) ? roleIDs : null)
		);
	}
}