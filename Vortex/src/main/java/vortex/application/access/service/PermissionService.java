package vortex.application.access.service;

import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

public interface PermissionService {
	public BoundedList<DataObject> search(DataObject req);
	
	public DataObject getInfo(String permissionID);
	
	public Permission getPermission(String permissionID);
	
	public boolean create(Permission permission);
	
	public boolean update(Permission permission);
	
	public BoundedList<DataObject> getActions(DataObject req);
	
	public int addActions(String permissionID, String... actionPaths);
	
	public int deleteActions(String permissionID, String... actionPaths);
	
	public int delete(String... permissionIDs);
}