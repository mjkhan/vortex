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
	public DataObject getGroups(DataObject req) {
		BoundedList<DataObject> groups = codeGroup.search(req);
		return dataobject()
			.set("groups", groups)
			.set("more", groups.hasNext())
			.set("next", groups.getEnd() + 1);
	}

	@Override
	public DataObject getGroup(DataObject req) {
		String groupID = req.string("groupID");
		return dataobject()
			.set("group", codeGroup.getGroup(groupID));
	}

	@Override
	public DataObject createGroup(DataObject req) {
		Group group = req.value("group");
		String userID = currentUser().getId();
		group.setCreatedBy(userID);
		group.setModifiedBy(userID);
		boolean saved = codeGroup.create(group) == 1;
		return dataobject()
			.set("saved", saved)
			.set("groupID", saved ? group.getId() : null);
	}

	@Override
	public DataObject updateGroup(DataObject req) {
		Group group = req.value("group");
		group.setModifiedBy(currentUser().getId());
		int saved = codeGroup.update(group);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject removeGroups(DataObject req) {
		String[] groupIDs = req.notEmpty("groupID").string("groupID").split(",");
		for (String groupID: groupIDs)
			codeMapper.deleteCodes(groupID);
		int saved = codeGroup.remove(groupIDs);
		return dataobject()
			.set("saved", saved > 0);
	}

	@Override
	public DataObject deleteGroups(DataObject req) {
		String s = req.string("groupID");
		String[] groupIDs = !isEmpty(s) ? s.split(",") : null;
		if (isEmpty(groupIDs))
			codeMapper.deleteCodes(null);
		else {
			for (String groupID: groupIDs)
				codeMapper.deleteCodes(groupID);
		}
		int saved = codeGroup.deleteGroups(groupIDs);
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
		code.setModifiedBy(currentUser().getId());
		int saved = codeMapper.create(code);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject updateCode(DataObject req) {
		Code code = req.value("code");
		code.setModifiedBy(currentUser().getId());
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