package vortex.application.group.service;

import vortex.application.ApplicationAccess;
import vortex.support.data.DataObject;

public interface GroupService extends ApplicationAccess {
	public DataObject getGroups(DataObject req);
	
	public DataObject getGroup(DataObject req);
	
	public DataObject createGroup(DataObject req);
	
	public DataObject updateGroup(DataObject req);
	
	public DataObject removeGroups(DataObject req);
	
	public DataObject deleteGroups(DataObject req);
}