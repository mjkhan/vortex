package vortex.application.code.service;

import java.util.List;
import java.util.Map;

import vortex.application.group.Group;
import vortex.support.data.DataObject;

public interface CodeService {
	public DataObject searchGroups(DataObject req);
	
	public Group getGroup(String groupID);
	
	public DataObject getGroupInfo(String groupID);
	
	public boolean create(Group group);
	
	public boolean update(Group group);
	
	public int removeGroups(String... groupIDs);
	
	public int deleteGroups(String... groupIDs);
	
	public DataObject getCodes(DataObject req);
	
	public Map<String, List<DataObject>> getCodesOf(String... groupIDs);
	
	public Code getCode(String groupID, String code);
	
	public boolean create(Code req);
	
	public boolean update(Code req);
	
	public int deleteCodes(String groupID, String... codes);
}