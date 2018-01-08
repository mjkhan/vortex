package vortex.application.access.service;

import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

public interface PermissionService {
	public BoundedList<DataObject> search(DataObject req);
	
	public DataObject getInfo(String permissionID);
	
	public Permission getPermission(String permissionID);
	
	public boolean create(Permission permission);
	
	public boolean update(Permission permission);
	
	public int addActions(String permissionID, String... actionIDs);
	
	public int deleteActions(String permissionID, String... actionIDs);
	
	public int delete(String... permissionIDs);
}