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
	public DataObject getInfo(String groupID) {
		return genericGroup.getInfo(groupID);
	}

	@Override
	public Group getGroup(String groupID) {
		return genericGroup.getGroup(groupID);
	}

	@Override
	public boolean create(Group group) {
		return genericGroup.create(group);
	}

	@Override
	public boolean update(Group group) {
		return genericGroup.update(group);
	}

	@Override
	public boolean removeGroups(String... groupIDs) {
		return genericGroup.remove(groupIDs) > 0;
	}

	@Override
	public boolean deleteGroups(String... groupIDs) {
		return genericGroup.deleteGroups(groupIDs) > 0;
	}
}
