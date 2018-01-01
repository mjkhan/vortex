package vortex.application.access.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.support.data.DataObject;

@Service("roleService")
public class RoleServiceImpl extends ApplicationService implements RoleService {
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RoleMemberMapper roleMemberMapper;

	@Override
	public DataObject getRoles(DataObject req) {
		return dataobject()
			.set("roles", roleMapper.getRoles());
	}

	@Override
	public DataObject getRolesFor(DataObject req) {
		return dataobject()
			.set("roles", roleMapper.getRolesFor(req.string("member")));
	}
	
	@Override
	public DataObject getInfo(String roleID) {
		return roleMapper.getInfo(roleID);
	}

	@Override
	public Role getRole(String roleID) {
		return roleMapper.getRole(roleID);
	}

	@Override
	public boolean create(Role role) {
		return roleMapper.create(role);
	}

	@Override
	public boolean update(Role role) {
		return roleMapper.update(role);
	}

	@Override
	public int delete(String... roleIDs) {
		return
			roleMemberMapper.deleteActions(roleIDs)
		  + roleMemberMapper.deleteUsers(roleIDs)
		  + roleMapper.remove(roleIDs);
	}

	@Override
	public DataObject getActions(DataObject req) {
		List<Map<String, Object>> actions = roleMemberMapper.getActions(req);
		return dataobject()
			.set("actions", actions);
	}

	@Override
	public DataObject addActions(DataObject req) {
		String[] roleIDs = req.value("roleIDs"),
				 actionIDs = req.value("actionIDs");
		int affected = roleMemberMapper.addActions(roleIDs, actionIDs);
		return dataobject()
			.set("affected", affected)
			.set("saved", affected > 0);
	}

	@Override
	public DataObject deleteActions(DataObject req) {
		String[] roleIDs = req.value("roleIDs"),
				 actionIDs = req.value("actionIDs");
		int affected = roleMemberMapper.deleteActions(roleIDs, actionIDs);
		return dataobject()
			.set("affected", affected)
			.set("saved", affected > 0);
	}

	@Override
	public DataObject getUsers(DataObject req) {
		List<Map<String, Object>> users = roleMemberMapper.getUsers(req);
		return dataobject()
			.set("users", users);
	}

	@Override
	public DataObject addUsers(DataObject req) {
		String[] roleIDs = req.value("roleIDs"),
				 userIDs = req.value("userIDs");
		int affected = roleMemberMapper.addUsers(roleIDs, userIDs);
		return dataobject()
			.set("affected", affected)
			.set("saved", affected > 0);
	}

	@Override
	public DataObject deleteUsers(DataObject req) {
		String[] roleIDs = req.value("roleIDs"),
				 userIDs = req.value("userIDs");
		int affected = roleMemberMapper.deleteUsers(roleIDs, userIDs);
		return dataobject()
			.set("affected", affected)
			.set("saved", affected > 0);
	}
}