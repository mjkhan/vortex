package vortex.application.group;

import java.util.List;

import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;
import vortex.support.data.Status;
import vortex.support.database.AbstractMapper;

public class GroupMapper extends AbstractMapper {
	private String groupType;
	
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	public BoundedList<DataObject> search(DataObject req) {
		DataObject params = ifEmpty(req, this::params);
		List<DataObject> list = selectList("group.search", params.set("groupType", groupType));
		return boundedList(list, params);
	}
	
	public Group getGroup(String groupID) {
		return selectOne(
			"group.getGroup"
			,params().set("groupType", groupType)
					 .set("groupID", groupID)
		);
	}
	
	public String create(Group group) {
		group.setType(groupType);
		String id = group.getId();
		if (isEmpty(id))
			group.setId(id = selectOne("group.newID", groupType));
		insert("group.insert", group);
		return id;
	}
	
	public int update(Group group) {
		group.setType(groupType);
		return update("group.update", group);
	}
	
	public int setStatus(String status, String... groupIDs) {
		return update(
			"group.setStatus"
		   , params().set("groupType", groupType)
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
	
	public int addMembers(String createdBy, String[] groupIDs, String memberType, String... memberIDs) {
		if (isEmpty(memberIDs)) return 0;
		
		return insert(
			"group.addMembers"
		   , params().set("groupType", groupType)
		   			 .set("groupIDs", groupIDs)
		   			 .set("memberType", memberType)
		   			 .set("memberIDs", memberIDs)
		   			 .set("createdBy", createdBy)
		);
	}
	
	public int addMembers(String createdBy, String groupID, String memberType, String... memberIDs) {
		String[] groupIDs = {groupID};
		return addMembers(createdBy, groupIDs, memberType, memberIDs);
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