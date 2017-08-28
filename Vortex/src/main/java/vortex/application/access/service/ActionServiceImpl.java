package vortex.application.access.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.group.Group;
import vortex.application.group.GroupMapper;
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
		return dataobject()
			.set("groups", actionGroup.search(req));
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
		String groupID = actionGroup.create(group);
		return dataobject()
			.set("saved", true)
			.set("groupID", groupID);
	}

	@Override
	public DataObject updateGroup(DataObject req) {
		Group group = req.value("group");
		int saved = actionGroup.update(group);
		return dataobject()
			.set("saved", saved == 1);
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
		int saved = actionMapper.create(action);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject updateAction(DataObject req) {
		Action action = req.value("action");
		int saved = actionMapper.update(action);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject deleteActions(DataObject req) {
		String actionID = req.string("actionID");
		String[] actionIDs = !isEmpty(actionID) ? actionID.split(",") : null;
		
		int saved = actionMapper.deleteActions(null, actionIDs);
		return dataobject()
			.set("saved", saved > 0);
	}
}