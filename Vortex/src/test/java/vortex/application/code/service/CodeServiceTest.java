package vortex.application.code.service;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import vortex.application.VortexTest;
import vortex.support.data.DataObject;

public class CodeServiceTest extends VortexTest {
	private CodeService codeMapper = getBean("codeService");
	
	private Code newCode(String group, String code, String value, String user) {
		Code obj = new Code();
		obj.setGroupID(group);
		obj.setCode(code);
		obj.setValue(value);
		obj.setModifiedBy(user);
		return obj;
	}
	
	@Test
	public void create() {
		String group = "001",
			   code = "code0",
			   value = "value0";
		Code code0 = newCode(group, code, value, "test user");
		codeMapper.createCode(dataObject().set("code", code0));
		Code loaded = codeMapper.getCode(dataObject().set("groupID", group).set("code", code)).value("code");
		Assert.assertNotNull(loaded);
		Assert.assertEquals(value, loaded.getValue());
	}

	@Test
	public void update() {
		String group = "001",
			   code = "code0",
			   value = "value0";
		Code code0 = newCode(group, code, value, "test user");
		codeMapper.createCode(dataObject().set("code", code0));
		Code loaded = codeMapper.getCode(dataObject().set("groupID", group).set("code", code)).value("code");
		Assert.assertNotNull(loaded);
		Assert.assertEquals(value, loaded.getValue());
		
		value = "value zero";
		String description = "code0 description";
		
		code0.setValue(value);
		code0.setDescription(description);
		codeMapper.updateCode(dataObject().set("code", code0));
		
		loaded = codeMapper.getCode(dataObject().set("groupID", group).set("code", code)).value("code");
		Assert.assertNotNull(loaded);
		Assert.assertEquals(value, loaded.getValue());
		Assert.assertEquals(description, loaded.getDescription());
	}

	@Test
	public void delete() {
		DataObject req = dataObject();
		String groupIDs = "001,002,003";
		String[] groups = groupIDs.split(",");
		for (String group: groups) {
			codeMapper.createCode(req.set("code", newCode(group, group + "-code0", group + "-value0", "test user")));
			codeMapper.createCode(req.set("code", newCode(group, group + "-code1", group + "-value1", "test user")));
		}
		req.clear();
		
		codeMapper.deleteCodes(req.set("groupID", "001").set("code", "001-code0"));
		req.clear();
		Map<String, List<DataObject>> codes = codeMapper.getCodesOf(req.set("groupID", groupIDs)).asMap("codes");
		List<DataObject> code001 = codes.get("001");
		Assert.assertEquals(1, code001.size());
		codeMapper.deleteCodes(req.set("groupID", "001"));
		codes = codeMapper.getCodesOf(req.set("groupID", groupIDs)).asMap("codes");
		code001 = codes.get("001");
		Assert.assertNull(code001);
	}
	
/*	
	@Test
	public void getCodes() {
		String[] groups = {"001", "002", "003"};
		for (String group: groups) {
			codeMapper.create(newCode(group, group + "-code0", group + "-value0", "test user"));
			codeMapper.create(newCode(group, group + "-code1", group + "-value1", "test user"));
		}
		
		Map<String, List<DataObject>> codes = codeMapper.getCodesOf(groups);
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
*/	
	@After
	public void teardown() {
		DataObject req = dataObject();
		for (int i = 1; i <= 3; ++i)
		codeMapper.deleteCodes(req.set("groupID", "00" + i));
	}
}
