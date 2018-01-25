package vortex.application.access.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.group.Group;
import vortex.application.group.GroupMapper;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Service("roleService")
public class RoleServiceImpl extends ApplicationService implements RoleService {
	@Resource(name="roleGroup")
	private GroupMapper roleMapper;
	
	@Override
	public BoundedList<DataObject> search(DataObject req) {
		return roleMapper.search(req);
	}

	@Override
	public DataObject getInfo(String roleID) {
		return roleMapper.getInfo(roleID);
	}

	@Override
	public Group getRole(String roleID) {
		return roleMapper.getGroup(roleID);
	}
	@Override
	public boolean create(Group role) {
		return roleMapper.create(role);
	}

	@Override
	public boolean update(Group role) {
		return roleMapper.update(role);
	}

	@Override
	public int delete(String... roleIDs) {
		return roleMapper.deleteGroups(roleIDs);
	}

	@Override
	public BoundedList<DataObject> getUsers(DataObject req) {
		return roleMapper.getMembers(
			"getUsers"
		   , req.set("memberType", USER)
		);
	}

	@Override
	public int addUsers(String[] roleIDs, String[] userIDs) {
		return roleMapper.addMembers(roleIDs, USER, userIDs);
	}

	@Override
	public int deleteUsers(String[] roleIDs, String[] userIDs) {
		return roleMapper.deleteMembers(roleIDs, USER, userIDs);
	}

	@Override
	public BoundedList<DataObject> getPermissions(DataObject req) {
		return roleMapper.getMembers(
			"getPermissions"
		   , req.set("memberType", PERMISSION)
		);
	}

	@Override
	public int addPermissions(String[] roleIDs, String[] permissionIDs) {
		return roleMapper.addMembers(roleIDs, PERMISSION, permissionIDs);
	}

	@Override
	public int deletePermissions(String[] roleIDs, String[] permissionIDs) {
		return roleMapper.deleteMembers(roleIDs, PERMISSION, permissionIDs);
	}
}