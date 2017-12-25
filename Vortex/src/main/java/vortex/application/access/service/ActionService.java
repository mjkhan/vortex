package vortex.application.access.service;

import vortex.support.data.DataObject;

public interface ActionService {
	public DataObject getGroups(DataObject req);
	
	public DataObject getGroup(DataObject req);
	
	public DataObject createGroup(DataObject req);
	
	public DataObject updateGroup(DataObject req);
	
	public DataObject removeGroups(DataObject req);
	
	public DataObject deleteGroups(DataObject req);
	
	public DataObject getActions(DataObject req);
	
	public DataObject getAction(DataObject req);
	
	public DataObject createAction(DataObject req);
	
	public DataObject updateAction(DataObject req);
	
	public DataObject deleteActions(DataObject req);
}