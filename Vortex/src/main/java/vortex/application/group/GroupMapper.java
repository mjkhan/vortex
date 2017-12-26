package vortex.application.group;

import java.util.List;

import vortex.application.DataMapper;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

public class GroupMapper extends DataMapper {
	private String groupType;
	
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	public BoundedList<DataObject> search(DataObject req) {
		DataObject params = ifEmpty(req, this::params);
		if (isEmpty(req.get("field")))
			req.remove("field");
		if (isEmpty(req.get("value")))
			req.remove("value");
		List<DataObject> list = selectList("group.search", params.set("groupType", groupType));
		return boundedList(list, params);
	}
	
	public DataObject getInfo(String groupID) {
		return selectOne(
			"group.getInfo"
			,params().set("groupType", groupType)
					 .set("groupID", groupID)
		);
	}
	
	public Group getGroup(String groupID) {
		return selectOne(
			"group.getGroup"
			,params().set("groupType", groupType)
					 .set("groupID", groupID)
		);
	}
	
	public int create(Group group) {
		if (group == null)
			return 0;
		
		group.setType(groupType);
		return insert("group.insert", params(true).set("group", group));
	}
	
	public int update(Group group) {
		if (group == null)
			return 0;
		
		group.setType(groupType);
		return update("group.update", params(true).set("group", group));
	}
	
	public int setStatus(String status, String... groupIDs) {
		return update(
			"group.setStatus"
		   , params(true)
		   		.set("groupType", groupType)
		   		.set("groupIDs", groupIDs)
		   		.set("status", status)
		);
	}
	
	public int remove(String... groupIDs) {
		return 
			deleteMembers(groupIDs, null)
		  + setStatus(Status.REMOVED.code(), groupIDs);
	}
	
	public int deleteGroups(String... groupIDs) {
		return
			deleteMembers(groupIDs, null)
		  + delete(
				"group.delete"
			   , params().set("groupType", groupType)
			   			 .set("groupIDs", groupIDs)
			);
	}
	
	public int addMembers(String[] groupIDs, String memberType, String... memberIDs) {
		if (isEmpty(memberIDs)) return 0;
		
		return insert(
			"group.addMembers"
		   , params(true)
		   		.set("groupType", groupType)
		   		.set("groupIDs", groupIDs)
		   		.set("memberType", memberType)
		   		.set("memberIDs", memberIDs)
		);
	}
	
	public int addMembers(String groupID, String memberType, String... memberIDs) {
		String[] groupIDs = {groupID};
		return addMembers(groupIDs, memberType, memberIDs);
	}
	
	public int deleteMembers(String[] groupIDs, String memberType, String... memberIDs) {
		return delete(
			"group.deleteMembers"
		   , params().set("groupType", groupType)
		   			 .set("groupIDs", !isEmpty(groupIDs) ? groupIDs : null)
		   			 .set("memberType", memberType)
		   			 .set("memberIDs", !isEmpty(memberIDs) ? memberIDs : null)
		);
	}
	
	public int deleteMembers(String groupID, String memberType, String... memberIDs) {
		String[] groupIDs = !isEmpty(groupID) ? new String[]{groupID} : null;
		return deleteMembers(groupIDs, memberType, memberIDs);
	}
	
	public int reorderMembers(String groupID, String memberType, String... memberIDs) {
		if (isEmpty(memberIDs)) return 0;
		
		return update(
			"group.reorderMembers"
		   , params().set("groupType", groupType)
		   			 .set("groupID", groupID)
		   			 .set("memberType", memberType)
		   			 .set("memberIDs", memberIDs)
		);
	}
}