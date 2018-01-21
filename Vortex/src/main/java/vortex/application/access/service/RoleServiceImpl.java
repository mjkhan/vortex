package vortex.application.access.service;

import java.util.List;
import java.util.Map;

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
	private RoleMemberMapper roleMemberMapper;
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
}