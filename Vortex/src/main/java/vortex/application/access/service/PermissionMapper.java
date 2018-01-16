package vortex.application.access.service;

import org.springframework.stereotype.Repository;

import vortex.application.DataMapper;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Repository("permissionMapper")
public class PermissionMapper extends DataMapper {
	public BoundedList<DataObject> search(DataObject params) {
		if (isEmpty(params.get("searchTerms"))) {
			params.remove("searchBy");
			params.remove("searchTerms");
		}
		return boundedList(
			selectList("permission.search", params)
		  , params
		);
	}
	
	public DataObject getInfo(String pmsID) {
		return selectOne("permission.getInfo", pmsID);
	}
	
	public Permission getPermission(String pmsID) {
		return selectOne("permission.getPermission", pmsID);
	}
	
	public boolean create(Permission permission) {
		return permission == null ? false :
			insert(
				"permission.insert"
			  , params(true).set("permission", permission)
			) == 1;
	}
	
	public boolean update(Permission permission) {
		return permission == null ? false :
			insert(
				"permission.update"
			  , params(true).set("permission", permission)
			) == 1;
	}

	public int delete(String... permissionIDs) {
		return
			deleteActions(null, permissionIDs, null)
		  + delete(
				"permission.delete"
			   , params().set("permissionIDs", permissionIDs)
			);
	}
	
	public BoundedList<DataObject> getActions(DataObject req) {
		DataObject params = params();
		params.putAll(req);
		return boundedList(
			selectList(
			"permission.getActions"
		   , params)
		   , params
		);
	}
	
	public int addActions(String[] permissionIDs, String[] actionIDs) {
		if (isEmpty(permissionIDs) || isEmpty(actionIDs)) return 0;
		return insert(
			"permission.addActions"
		   ,params(true)
		   	.set("permissionIDs", permissionIDs)
		   	.set("actionIDs", actionIDs)
		);
	}
	
	public int addActions(String permissionID, String... actionIDs) {
		return !isEmpty(permissionID) ?
			addActions(new String[]{permissionID}, actionIDs) :
			0;
	}
	
	private int deleteActions(String[] groupIDs, String[] permissionIDs, String[] actionIDs) {
		return delete(
			"permission.deleteActions"
		   ,params()
		   	.set("groupIDs", ifEmpty(groupIDs, () -> null))
		   	.set("actionIDs", ifEmpty(actionIDs, () -> null))
		   	.set("permissionIDs", ifEmpty(permissionIDs, () -> null))
		);
	}
	
	public int deleteActions(String permissionID, String... actionIDs) {
		return deleteActions(null, new String[]{permissionID}, actionIDs);
	}
	
	public int deleteActions(String... groupIDs) {
		return deleteActions(groupIDs, null, null);
	}
}