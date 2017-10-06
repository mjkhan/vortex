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
	public DataObject getRole(DataObject req) {
		String roleID = req.string("roleID");
		return dataobject()
			.set("role", roleMapper.getRole(roleID));
	}

	@Override
	public DataObject create(DataObject req) {
		Role role = req.value("role");
		role.setModifiedBy(currentUser().getId());
		String roleID = roleMapper.create(role);
		return dataobject()
			.set("saved", true)
			.set("roleID", roleID);
	}

	@Override
	public DataObject update(DataObject req) {
		Role role = req.value("role");
		role.setModifiedBy(currentUser().getId());
		int saved = roleMapper.update(role);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject delete(DataObject req) {
		String s = req.string("roleID");
		String[] roleIDs = !isEmpty(s) ? s.split(",") : null;
		roleMemberMapper.deleteActions(roleIDs);
		roleMemberMapper.deleteUsers(roleIDs, null);
		int saved = roleMapper.remove(roleIDs);
		return dataobject()
			.set("saved", saved > 0);
	}

	@Override
	public DataObject getActions(DataObject req) {
		List<Map<String, Object>> actions = roleMemberMapper.getActions(req);
		return dataobject()
			.set("actions", actions);
	}

	@Override
	public DataObject addActions(DataObject req) {
		String addedBy = currentUser().getId();
		String[] roleIDs = req.value("roleIDs"),
				 actionIDs = req.value("actionIDs");
		int affected = roleMemberMapper.addActions(addedBy, roleIDs, actionIDs);
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
		String addedBy = currentUser().getId();
		String[] roleIDs = req.value("roleIDs"),
				 userIDs = req.value("userIDs");
		int affected = roleMemberMapper.addUsers(addedBy, roleIDs, userIDs);
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