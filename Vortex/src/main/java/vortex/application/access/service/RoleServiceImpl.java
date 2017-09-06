package vortex.application.access.service;

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
}