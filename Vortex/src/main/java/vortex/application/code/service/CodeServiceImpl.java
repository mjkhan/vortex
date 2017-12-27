package vortex.application.code.service;

import java.util.List;
import java.util.Map;

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
	public DataObject searchGroups(DataObject req) {
		BoundedList<DataObject> groups = codeGroup.search(req);
		return dataobject()
			.set("groups", groups)
			.set("more", groups.hasNext())
			.set("next", groups.getEnd() + 1);
	}
	
	@Override
	public DataObject getGroupInfo(String groupID) {
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
			affected += codeMapper.deleteCodes(groupID);
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
	public DataObject getCodes(DataObject req) {
		//TODO:페이징 처리 추가
		String[] groupIDs = req.string("groupID").split(",");
		return dataobject()
			.set("codes", codeMapper.getCodes(groupIDs));
	}

	@Override
	public Map<String, List<DataObject>> getCodesOf(String... groupIDs) {
		return codeMapper.getCodesOf(groupIDs);
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
	public int deleteCodes(String groupID, String... codes) {
		return codeMapper.deleteCodes(groupID, codes);
	}
}