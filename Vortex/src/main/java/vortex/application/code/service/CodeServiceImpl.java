package vortex.application.code.service;

import javax.annotation.Resource;

import vortex.application.ApplicationService;
import vortex.application.group.Group;
import vortex.application.group.GroupMapper;
import vortex.support.data.DataObject;

public class CodeServiceImpl extends ApplicationService implements CodeService {
	@Resource(name="codeGroup")
	private GroupMapper codeGroup;
	
	@Override
	public DataObject getGroups(DataObject req) {
		return dataobject()
			.set("codeGroups", codeGroup.search(req));
	}

	@Override
	public DataObject getGroup(DataObject req) {
		String groupID = req.string("groupID");
		return dataobject()
			.set("codeGroup", codeGroup.getGroup(groupID));
	}

	@Override
	public DataObject createGroup(DataObject req) {
		Group group = req.value("group");
		String groupID = codeGroup.create(group);
		return dataobject()
			.set("saved", true)
			.set("groupID", groupID);
	}

	@Override
	public DataObject updateGroup(DataObject req) {
		Group group = req.value("group");
		int saved = codeGroup.update(group);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject deleteGroups(DataObject req) {
		String[] groupIDs = req.string("groupIDs").split(",");
		for (String groupID: groupIDs)
			codeMapper.deleteCodes(groupID);
		int saved = codeGroup.remove(groupIDs);
		return dataobject()
			.set("saved", saved > 0);
	}

	@Override
	public DataObject getCodes(DataObject req) {
		String[] groupIDs = req.string("groupID").split(",");
		return dataobject()
			.set("codes", codeMapper.getCodes(groupIDs));
	}

	@Override
	public DataObject getCodesOf(DataObject req) {
		String[] groupIDs = req.string("groupID").split(",");
		return dataobject()
			.set("codes", codeMapper.getCodesOf(groupIDs));
	}

	@Override
	public DataObject getCode(DataObject req) {
		String groupID = req.string("groupID"),
			   code = req.string("code");
		return dataobject()
			.set("code", codeMapper.getCode(groupID, code));
	}

	@Override
	public DataObject createCode(DataObject req) {
		Code code = req.value("code");
		int saved = codeMapper.create(code);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject updateCode(DataObject req) {
		Code code = req.value("code");
		int saved = codeMapper.update(code);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject deleteCodes(DataObject req) {
		String groupID = req.string("groupID"),
			   code = req.string("code");
		int saved = codeMapper.deleteCodes(
			groupID,
			!isEmpty(code) ? code.split(",") : null
			);
		return dataobject()
			.set("saved", saved > 0);
	}
}