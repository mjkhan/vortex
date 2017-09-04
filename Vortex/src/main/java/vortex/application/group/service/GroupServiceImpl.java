package vortex.application.group.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.group.Group;
import vortex.application.group.GroupMapper;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Service("groupService")
public class GroupServiceImpl extends ApplicationService implements GroupService {
	@Resource(name="genericGroup")
	private GroupMapper genericGroup;
	@Override
	public DataObject getGroups(DataObject req) {
		BoundedList<DataObject> groups = genericGroup.search(req);
		return dataobject()
			.set("groups", groups)
			.set("more", groups.hasNext())
			.set("next", groups.getEnd() + 1);
	}

	@Override
	public DataObject getGroup(DataObject req) {
		String groupID = req.string("groupID");
		return dataobject()
			.set("group", genericGroup.getGroup(groupID));
	}

	@Override
	public DataObject createGroup(DataObject req) {
		Group group = req.value("group");
		String userID = currentUser().getId();
		group.setCreatedBy(userID);
		group.setModifiedBy(userID);
		String groupID = genericGroup.create(group);
		return dataobject()
			.set("saved", true)
			.set("groupID", groupID);
	}

	@Override
	public DataObject updateGroup(DataObject req) {
		Group group = req.value("group");
		group.setModifiedBy(currentUser().getId());
		int saved = genericGroup.update(group);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject removeGroups(DataObject req) {
		String[] groupIDs = req.notEmpty("groupID").string("groupID").split(",");
		int saved = genericGroup.remove(groupIDs);
		return dataobject()
			.set("saved", saved > 0);
	}

	@Override
	public DataObject deleteGroups(DataObject req) {
		String s = req.string("groupID");
		String[] groupIDs = !isEmpty(s) ? s.split(",") : null;
		int saved = genericGroup.deleteGroups(groupIDs);
		return dataobject()
			.set("saved", saved > 0);
	}

}
