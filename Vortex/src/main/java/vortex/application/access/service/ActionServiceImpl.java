package vortex.application.access.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.group.Group;
import vortex.application.group.GroupMapper;
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
	public List<DataObject> getGroups(DataObject req) {
		return actionGroup.search(req);
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
	public List<DataObject> getActions(String groupID) {
		return actionMapper.getActions(groupID);
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
	public Permission.Status getPermission(String userID, String actionPath) {
		if (checkAccessPermission == null)
			checkAccessPermission = "enable".equalsIgnoreCase(properties.getString("accessPermission"));
		if (!checkAccessPermission)
			return Permission.Status.GRANTED;
		
		if (permitAll == null)
			permitAll = Arrays.asList(properties.getStringArray("permitAll"));
		if (permitAll.contains(actionPath)) return Permission.Status.GRANTED;
		
		log().debug(() -> "Getting permission for " + userID + "to " + actionPath);
		return
			roleMemberMapper.isPermitted(userID, actionPath) ? Permission.Status.GRANTED :
			actionMapper.findAction(actionPath) != null ? Permission.Status.DENIED :
			Permission.Status.ACTION_NOT_FOUND;
	}
}