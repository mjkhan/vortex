package vortex.application.access.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.group.Group;
import vortex.application.group.GroupMapper;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

@Service("actionService")
public class ActionServiceImpl extends ApplicationService implements ActionService {
	@Resource(name="actionGroup")
	private GroupMapper actionGroup;
	@Autowired
	private ActionMapper actionMapper;
	@Autowired
	private RoleMemberMapper roleMemberMapper;

	@Override
	public DataObject getGroups(DataObject req) {
		BoundedList<DataObject> groups = actionGroup.search(req);
		return dataobject()
			.set("groups", groups)
			.set("totalSize", groups.getTotalSize())
			.set("fetchSize", groups.getFetchSize())
			.set("more", groups.hasNext())
			.set("next", groups.getEnd() + 1);
	}

	@Override
	public DataObject getGroupInfo(String groupID) {
		return actionGroup.getInfo(groupID);
	}

	@Override
	public Group getGroup(String groupID) {
		return actionGroup.getGroup(groupID);
	}

	@Override
	public boolean create(Group group) {
		return actionGroup.create(group);
	}

	@Override
	public boolean update(Group group) {
		return actionGroup.update(group);
	}
	
	@Override
	public int setGroupStatus(String status, String... groupIDs) {
		int affected = roleMemberMapper.deleteActionsOfGroups(groupIDs);
		for (String groupID: groupIDs) {
			affected += actionMapper.setStatus(status, groupID);
		}
		return affected += actionGroup.setStatus(status, groupIDs);
	}

	@Override
	public int deleteGroups(String... groupIDs) {
		int affected = roleMemberMapper.deleteActionsOfGroups(groupIDs);
		if (isEmpty(groupIDs)) {
			affected += actionMapper.deleteActions(null);
		} else {
			for (String groupID: groupIDs)
				affected += actionMapper.deleteActions(groupID);
		}
		return affected += actionGroup.deleteGroups(groupIDs);
	}

	@Override
	public DataObject getActions(DataObject req) {
		String groupID = req.string("groupID");
		return dataobject()
			.set("actions", actionMapper.getActions(groupID));
	}

	@Override
	public DataObject getInfo(String actionID) {
		return actionMapper.getInfo(actionID);
	}

	@Override
	public Action getAction(String actionID) {
		return actionMapper.getAction(actionID);
	}

	@Override
	public boolean create(Action action) {
		return actionMapper.create(action);
	}

	@Override
	public boolean update(Action action) {
		return actionMapper.update(action);
	}

	@Override
	public int setStatus(String status, String... actionIDs) {
		int affected = 0;
		if (Status.REMOVED.code().equals(status))
			affected += roleMemberMapper.deleteActions(null, actionIDs);
		return affected += actionMapper.setStatus(status, null, actionIDs);
	}

	@Override
	public int deleteActions(String groupID, String... actionIDs) {
		if (isEmpty(actionIDs))
			return deleteGroups(groupID);
		return roleMemberMapper.deleteActions(null, actionIDs)
			 + actionMapper.deleteActions(null, actionIDs);
	}
	
	private static List<String> permitAll;
	private static Boolean checkAccessPermission;
	
	@Override
	public Action.Permission getPermission(String userID, String actionPath) {
		if (checkAccessPermission == null)
			checkAccessPermission = "enable".equalsIgnoreCase(properties.getString("accessPermission"));
		if (!checkAccessPermission)
			return Action.Permission.GRANTED;
		
		if (permitAll == null)
			permitAll = Arrays.asList(properties.getStringArray("permitAll"));
		if (permitAll.contains(actionPath)) return Action.Permission.GRANTED;
		
		log().debug(() -> "Getting permission for " + userID + "to " + actionPath);
		return
			roleMemberMapper.isPermitted(userID, actionPath) ? Action.Permission.GRANTED :
			actionMapper.findAction(actionPath) != null ? Action.Permission.DENIED :
			Action.Permission.NOT_FOUND;
	}
}