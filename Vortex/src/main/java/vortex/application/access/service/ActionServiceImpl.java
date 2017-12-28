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
	public DataObject getGroup(DataObject req) {
		String groupID = req.string("groupID");
		return dataobject()
			.set("group", actionGroup.getGroup(groupID));
	}

	@Override
	public DataObject createGroup(DataObject req) {
		Group group = req.value("group");
		String userID = currentUser().getId();
		group.setCreatedBy(userID);
		group.setModifiedBy(userID);
		boolean saved = actionGroup.create(group);
		return dataobject()
			.set("saved", saved)
			.set("groupID", saved ? group.getId() : null);
	}

	@Override
	public DataObject updateGroup(DataObject req) {
		Group group = req.value("group");
		String userID = currentUser().getId();
		group.setModifiedBy(userID);
		return dataobject()
			.set("saved", actionGroup.update(group));
	}

	@Override
	public DataObject removeGroups(DataObject req) {
		String[] groupIDs = req.string("groupID").split(",");
		
		roleMemberMapper.deleteActionsOfGroups(groupIDs);
		for (String groupID: groupIDs) {
			actionMapper.deleteActions(groupID);
		}
		int saved = actionGroup.remove(groupIDs);
		return dataobject()
				.set("saved", saved > 0);
	}

	@Override
	public DataObject deleteGroups(DataObject req) {
		String s = req.string("groupID");
		String[] groupIDs = !isEmpty(s) ? s.split(",") : null;
		
		roleMemberMapper.deleteActionsOfGroups(groupIDs);
		if (isEmpty(s)) {
			actionMapper.deleteActions(null);
		} else {
			for (String groupID: groupIDs)
				actionMapper.deleteActions(groupID);
		}
		int saved = actionGroup.deleteGroups(groupIDs);
		return dataobject()
				.set("saved", saved > 0);
	}

	@Override
	public DataObject getActions(DataObject req) {
		String groupID = req.string("groupID");
		return dataobject()
			.set("actions", actionMapper.getActions(groupID));
	}

	@Override
	public DataObject getAction(DataObject req) {
		String actionID = req.string("actionID");
		return dataobject()
			.set("action", actionMapper.getAction(actionID));
	}

	@Override
	public DataObject createAction(DataObject req) {
		Action action = req.value("action");
		String userID = currentUser().getId();
		action.setModifiedBy(userID);

		String id = actionMapper.create(action);
		return dataobject()
			.set("actionID", id)
			.set("saved", !isEmpty(id));
	}

	@Override
	public DataObject updateAction(DataObject req) {
		Action action = req.value("action");
		String userID = currentUser().getId();
		action.setModifiedBy(userID);

		int saved = actionMapper.update(action);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject deleteActions(DataObject req) {
		String actionID = req.string("actionID");
		String[] actionIDs = !isEmpty(actionID) ? actionID.split(",") : null;
		roleMemberMapper.deleteActions(null, actionIDs);
		int saved = actionMapper.deleteActions(null, actionIDs);
		return dataobject()
			.set("saved", saved > 0);
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