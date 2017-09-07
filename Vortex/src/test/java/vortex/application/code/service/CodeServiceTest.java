package vortex.application.code.service;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import vortex.application.VortexTest;
import vortex.application.group.Group;
import vortex.support.data.DataObject;

public class CodeServiceTest extends VortexTest {
	private CodeService codeService = getBean("codeService");
	
	private Group newGroup(int num) {
		Group group = new Group();
		group.setName("code group " + num);
		String userID = "test user";
		group.setCreatedBy(userID);
		group.setModifiedBy(userID);
		return group;
	}
	
	private Code newCode(String group, String code, String value, String user) {
		Code obj = new Code();
		obj.setGroupID(group);
		obj.setCode(code);
		obj.setValue(value);
		obj.setModifiedBy(user);
		return obj;
	}
	
	@Test
	public void createGroup() {
		String groupID = codeService.createGroup(dataObject().set("group", newGroup(0))).string("groupID");
		Group group = codeService.getGroup(dataObject().set("groupID", groupID)).value("group");
		Assert.assertNotNull(group);
		Assert.assertEquals(groupID, group.getId());
	}
	
	@Test
	public void updateGroup() {
		String groupID = codeService.createGroup(dataObject().set("group", newGroup(0))).string("groupID");
		Group group = codeService.getGroup(dataObject().set("groupID", groupID)).value("group");
		Assert.assertNotNull(group);
		Assert.assertEquals(groupID, group.getId());
		
		String name = "new group name";
		group.setName(name);
		codeService.updateGroup(dataObject().set("group", group));
		group = codeService.getGroup(dataObject().set("groupID", groupID)).value("group");
		Assert.assertNotNull(group);
		Assert.assertEquals(name, group.getName());
	}
	
	@Test
	public void removeGroups() {
		String groupID0 = codeService.createGroup(dataObject().set("group", newGroup(0))).string("groupID");
		String groupID1 = codeService.createGroup(dataObject().set("group", newGroup(1))).string("groupID");
		
		codeService.removeGroups(dataObject().set("groupID", groupID0 + "," + groupID1));
		Group group = codeService.getGroup(dataObject().set("groupID", groupID0)).value("group");
		Assert.assertEquals("999", group.getStatus());
		group = codeService.getGroup(dataObject().set("groupID", groupID1)).value("group");
		Assert.assertEquals("999", group.getStatus());
	}
	
	@Test
	public void getGroups() {}
	
	@Test
	public void createCode() {
		String group = "001",
			   code = "code0",
			   value = "value0";
		Code code0 = newCode(group, code, value, "test user");
		codeService.createCode(dataObject().set("code", code0));
		Code loaded = codeService.getCode(dataObject().set("groupID", group).set("code", code)).value("code");
		Assert.assertNotNull(loaded);
		Assert.assertEquals(value, loaded.getValue());
	}

	@Test
	public void updateCode() {
		String group = "001",
			   code = "code0",
			   value = "value0";
		Code code0 = newCode(group, code, value, "test user");
		codeService.createCode(dataObject().set("code", code0));
		Code loaded = codeService.getCode(dataObject().set("groupID", group).set("code", code)).value("code");
		Assert.assertNotNull(loaded);
		Assert.assertEquals(value, loaded.getValue());
		
		value = "value zero";
		String description = "code0 description";
		
		code0.setValue(value);
		code0.setDescription(description);
		codeService.updateCode(dataObject().set("code", code0));
		
		loaded = codeService.getCode(dataObject().set("groupID", group).set("code", code)).value("code");
		Assert.assertNotNull(loaded);
		Assert.assertEquals(value, loaded.getValue());
		Assert.assertEquals(description, loaded.getDescription());
	}

	@Test
	public void deleteCode() {
		DataObject req = dataObject();
		String groupIDs = "001,002,003";
		String[] groups = groupIDs.split(",");
		for (String group: groups) {
			codeService.createCode(req.set("code", newCode(group, group + "-code0", group + "-value0", "test user")));
			codeService.createCode(req.set("code", newCode(group, group + "-code1", group + "-value1", "test user")));
		}
		req.clear();
		
		codeService.deleteCodes(req.set("groupID", "001").set("code", "001-code0"));
		req.clear();
		Map<String, List<DataObject>> codes = codeService.getCodesOf(req.set("groupID", groupIDs)).value("codes");
		List<DataObject> code001 = codes.get("001");
		Assert.assertEquals(1, code001.size());
		codeService.deleteCodes(req.set("groupID", "001"));
		codes = codeService.getCodesOf(req.set("groupID", groupIDs)).value("codes");
		code001 = codes.get("001");
		Assert.assertNull(code001);
	}
	
	@Test
	public void getCodes() {
		String groupIDs = "001,002,003";
		String[] groups = groupIDs.split(",");
		DataObject req = dataObject();
		for (String group: groups) {
			codeService.createCode(req.set("code", newCode(group, group + "-code0", group + "-value0", "test user")));
			codeService.createCode(req.set("code", newCode(group, group + "-code1", group + "-value1", "test user")));
		}
		
		List<DataObject> codes = codeService.getCodes(req.set("groupID", groupIDs)).value("codes");
		Assert.assertEquals(6, codes.size());
	}
	
	@Test
	public void getCodesOf() {
		String groupIDs = "001,002,003";
		String[] groups = groupIDs.split(",");
		DataObject req = dataObject();
		for (String group: groups) {
			codeService.createCode(req.set("code", newCode(group, group + "-code0", group + "-value0", "test user")));
			codeService.createCode(req.set("code", newCode(group, group + "-code1", group + "-value1", "test user")));
		}
		
		Map<String, List<DataObject>> codes = codeService.getCodesOf(req.set("groupID", groupIDs)).value("codes");
		for (String group: groups) {
			List<DataObject> codeValues = codes.get(group);
			Assert.assertEquals(2, codeValues.size());
			for (int i = 0; i < codeValues.size(); ++i) {
				DataObject row = codeValues.get(i);
				Assert.assertEquals(group + "-code" + i, row.get("cd_id"));
				Assert.assertEquals(group + "-value" + i, row.get("cd_val"));
			}
		}
	}

	@After
	public void teardown() {
		DataObject req = dataObject();
		codeService.deleteGroups(req.set("groupID", "001,002,003"));
	}
}
