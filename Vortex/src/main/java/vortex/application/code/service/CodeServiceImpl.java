package vortex.application.code.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.group.Group;
import vortex.application.group.GroupMapper;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Service("codeService")
public class CodeServiceImpl extends ApplicationService implements CodeService {
	@Resource(name="codeGroup")
	private GroupMapper codeGroup;
	
	@Override
	public BoundedList<DataObject> searchGroups(DataObject req) {
		return codeGroup.search(req);
	}
	
	@Override
	public DataObject getInfo(String groupID) {
		return codeGroup.getInfo(groupID);
	}

	@Override
	public Group getGroup(String groupID) {
		return codeGroup.getGroup(groupID);
	}

	@Override
	public boolean create(Group group) {
		return codeGroup.create(group);
	}

	@Override
	public boolean update(Group group) {
		return codeGroup.update(group);
	}

	@Override
	public int removeGroups(String... groupIDs) {
		int affected = 0;
		for (String groupID: groupIDs)
			affected += codeMapper.remove(groupID);
		return affected += codeGroup.remove(groupIDs);
	}

	@Override
	public int deleteGroups(String... groupIDs) {
		int affected = 0;
		if (isEmpty(groupIDs))
			affected += codeMapper.deleteCodes(null);
		else {
			for (String groupID: groupIDs)
				affected += codeMapper.deleteCodes(groupID);
		}
		return affected += codeGroup.deleteGroups(groupIDs);
	}

	@Override
	public BoundedList<DataObject> getCodes(DataObject req) {
		return codeMapper.getCodes(req);
	}
/*
	@Override
	public Map<String, List<DataObject>> getCodesOf(String... groupIDs) {
		return codeMapper.getCodesOf(groupIDs);
	}
*/	
	@Override
	public DataObject getInfo(String groupID, String code) {
		return codeMapper.getInfo(groupID, code);
	}

	@Override
	public Code getCode(String groupID, String code) {
		return codeMapper.getCode(groupID, code);
	}

	@Override
	public boolean create(Code code) {
		return codeMapper.create(code);
	}

	@Override
	public boolean update(Code code) {
		return codeMapper.update(code);
	}
	
	@Override
	public int removeCodes(String groupID, String... codes) {
		return codeMapper.remove(groupID, codes);
	}

	@Override
	public int deleteCodes(String groupID, String... codes) {
		return codeMapper.deleteCodes(groupID, codes);
	}
}