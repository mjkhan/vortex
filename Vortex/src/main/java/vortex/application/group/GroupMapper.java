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
	
	@Override
	protected DataObject params(boolean currentUser) {
		return super.params(currentUser)
					.set("groupType", groupType);
	}
	
	public BoundedList<DataObject> search(DataObject req) {
		DataObject params = ifEmpty(req, this::params);
		if (isEmpty(req.get("searchBy")))
			req.remove("searchBy");
		if (isEmpty(req.get("searchTerms")))
			req.remove("searchTerms");
		List<DataObject> list = selectList("group.search", params.set("groupType", groupType));
		return boundedList(list, params);
	}
	
	public DataObject getInfo(String groupID) {
		return selectOne(
			"group.getInfo"
			,params().set("groupID", groupID)
		);
	}
	
	public Group getGroup(String groupID) {
		return selectOne(
			"group.getGroup"
			,params().set("groupID", groupID)
		);
	}
	
	public boolean create(Group group) {
		if (group == null) return false;
		
		group.setType(groupType);
		return insert("group.insert", params(true).set("group", group)) == 1;
	}
	
	public boolean update(Group group) {
		if (group == null) return false;
		
		group.setType(groupType);
		return update("group.update", params(true).set("group", group)) == 1;
	}
	
	public int setStatus(String status, String... groupIDs) {
		return update(
			"group.setStatus"
		   , params(true)
		   		.set("groupIDs", groupIDs)
		   		.set("status", status)
		);
	}
	
	public int remove(String... groupIDs) {
		return 
			deleteMembers(groupIDs, null, (String[])null)
		  + setStatus(Status.REMOVED.code(), groupIDs);
	}
	
	public int deleteGroups(String... groupIDs) {
		return
			deleteMembers(groupIDs, null, (String[])null)
		  + delete(
				"group.delete"
			   , params().set("groupIDs", groupIDs)
			);
	}
	
	public BoundedList<DataObject> getMembers(String queryID, DataObject req) {
		if (!queryID.contains("."))
			queryID = "group." + queryID;
		return boundedList(
			selectList(queryID, req.set("groupType", groupType))
		  , req
		);
	}
	
	public List<DataObject> getGroupInfoOfMembers(String memberType, String... memberIDs) {
		return selectList(
			"group.groupInfoOfMembers"
		   , params().set("memberType", memberType)
		   			 .set("memberIDs", ifEmpty(memberIDs, null))
		);
	}
	
	public List<Group> getGroupsOfMembers(String memberType, String... memberIDs) {
		return selectList(
			"group.groupsOfMembers"
		   , params().set("memberType", memberType)
		   			 .set("memberIDs", ifEmpty(memberIDs, null))
		);
	}

	public int addMembers(String[] groupIDs, String memberType, String... memberIDs) {
		if (isEmpty(memberIDs)) return 0;
		
		return insert(
			"group.addMembers"
		   , params(true)
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
		   , params().set("groupIDs", ifEmpty(groupIDs, null))
		   			 .set("memberType", ifEmpty(memberType, null))
		   			 .set("memberIDs", ifEmpty(memberIDs, null))
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
		   , params().set("groupID", groupID)
		   			 .set("memberType", memberType)
		   			 .set("memberIDs", memberIDs)
		);
	}
}