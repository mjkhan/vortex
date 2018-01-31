package vortex.application.access.service;

import java.util.List;

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
	
	public List<Permission> getPermissions(String userID) {
		return selectList("permission.getPermissions", userID);
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
/*	
	public int delete(String[] groupIDs, String[] permissionIDs) {
		deleteActions(groupIDs, permissionIDs, null);
		return delete(
			"permission.delete"
		   , params()
		   	.set("groupIDs", ifEmpty(groupIDs, null))
		   	.set("permissionIDs", ifEmpty(permissionIDs, null))
		);
	}
*/
	public int delete(String... permissionIDs) {
		deleteActions(permissionIDs, null);
		return delete(
			"permission.delete"
		   , params()
		   	.set("permissionIDs", ifEmpty(permissionIDs, null))
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
	
	public int addActions(String[] permissionIDs, String[] actionPaths) {
		if (isEmpty(permissionIDs) || isEmpty(actionPaths)) return 0;
		return insert(
			"permission.addActions"
		   ,params(true)
		   	.set("permissionIDs", permissionIDs)
		   	.set("actionPaths", actionPaths)
		);
	}
	
	public int addActions(String permissionID, String... actionPaths) {
		return !isEmpty(permissionID) ?
			addActions(new String[]{permissionID}, actionPaths) :
			0;
	}
/*	
	private int deleteActions(String[] groupIDs, String[] permissionIDs, String[] actionPaths) {
		return delete(
			"permission.deleteActions"
		   ,params()
		   	.set("groupIDs", ifEmpty(groupIDs, () -> null))
		   	.set("actionPaths", ifEmpty(actionPaths, () -> null))
		   	.set("permissionIDs", ifEmpty(permissionIDs, () -> null))
		);
	}
*/	
	private int deleteActions(String[] permissionIDs, String[] actionPaths) {
		return delete(
			"permission.deleteActions"
		   ,params()
		   	.set("actionPaths", ifEmpty(actionPaths, () -> null))
		   	.set("permissionIDs", ifEmpty(permissionIDs, () -> null))
		);
	}

	public int deleteActions(String permissionID, String... actionPaths) {
		return !isEmpty(permissionID) ?
			deleteActions(new String[]{permissionID}, actionPaths)
		  : 0;
	}
/*	
	public int deleteActions(String... groupIDs) {
		return deleteActions(groupIDs, null, null);
	}
*/		
	public boolean isPermitted(String userID, String actionPath) {
		int count = selectOne(
			"permission.countUserPermissionsForAction"
		  , params().set("userID", userID)
					.set("actionPath", actionPath)
		);
		return count > 0;
	}
}