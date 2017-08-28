package vortex.application.code.service;

import vortex.application.ApplicationAccess;
import vortex.support.data.DataObject;

public interface CodeService extends ApplicationAccess {
	public DataObject getGroups(DataObject req);
	
	public DataObject getGroup(DataObject req);
	
	public DataObject createGroup(DataObject req);
	
	public DataObject updateGroup(DataObject req);
	
	public DataObject removeGroups(DataObject req);
	
	public DataObject deleteGroups(DataObject req);
	
	public DataObject getCodes(DataObject req);
	
	public DataObject getCodesOf(DataObject req);
	
	public DataObject getCode(DataObject req);
	
	public DataObject createCode(DataObject req);
	
	public DataObject updateCode(DataObject req);
	
	public DataObject deleteCodes(DataObject req);
}