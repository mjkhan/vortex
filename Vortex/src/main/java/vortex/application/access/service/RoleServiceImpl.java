package vortex.application.access.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.group.Group;
import vortex.application.group.GroupMapper;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Service("roleService")
public class RoleServiceImpl extends ApplicationService implements RoleService {
	@Autowired
	private RoleMemberMapper roleMemberMapper; //TODO:DELETE
	@Autowired
	private GroupMapper roleGroup;
	
	@Override
	public BoundedList<DataObject> search(DataObject req) {
		return roleGroup.search(req);
	}

	@Override
	public DataObject getInfo(String roleID) {
		return roleGroup.getInfo(roleID);
	}

	@Override
	public Group getRole(String roleID) {
		return roleGroup.getGroup(roleID);
	}
	@Override
	public boolean create(Group role) {
		return roleGroup.create(role);
	}

	@Override
	public boolean update(Group role) {
		return roleGroup.update(role);
	}

	@Override
	public int delete(String... roleIDs) {
		return roleGroup.deleteGroups(roleIDs);
	}

	@Override
	public BoundedList<DataObject> getUsers(DataObject req) {
		return roleGroup.getMembers(
			"getUsers"
		   , req.set("memberType", USER)
		);
	}

	@Override
	public int addUsers(String[] roleIDs, String[] userIDs) {
		return roleGroup.addMembers(roleIDs, USER, userIDs);
	}

	@Override
	public int deleteUsers(String[] roleIDs, String[] userIDs) {
		return roleGroup.deleteMembers(roleIDs, USER, userIDs);
	}

	@Override
	public BoundedList<DataObject> getPermissions(DataObject req) {
		return roleGroup.getMembers(
			"getPermissions"
		   , req.set("memberType", PERMISSION)
		);
	}

	@Override
	public int addPermissions(String[] roleIDs, String[] permissionIDs) {
		return roleGroup.addMembers(roleIDs, PERMISSION, permissionIDs);
	}

	@Override
	public int deletePermissions(String[] roleIDs, String[] permissionIDs) {
		return roleGroup.deleteMembers(roleIDs, PERMISSION, permissionIDs);
	}
}