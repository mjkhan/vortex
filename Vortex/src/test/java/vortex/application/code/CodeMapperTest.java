package vortex.application.code;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import vortex.application.VortexTest;
import vortex.support.data.DataObject;

public class CodeMapperTest extends VortexTest {
	private CodeMapper codeMapper = getBean("codeMapper");
	
	private Code newCode(String group, String code, String value, String user) {
		Code obj = new Code();
		obj.setGroupID(group);
		obj.setCode(code);
		obj.setValue(value);
		obj.setModifiedBy(user);
		return obj;
	}
	
	@Test
	public void getCodes() {
		String[] groups = {"001", "002", "003"};
		for (String group: groups) {
			codeMapper.create(newCode(group, group + "-code0", group + "-value0", "test user"));
			codeMapper.create(newCode(group, group + "-code1", group + "-value1", "test user"));
		}
		
		Map<String, List<DataObject>> codes = codeMapper.getCodes(groups);
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
	
	@Test
	public void create() {
		String group = "001",
			   code = "code0",
			   value = "value0";
		Code code0 = newCode(group, code, value, "test user");
		codeMapper.create(code0);
		Code loaded = codeMapper.getCode(group, code);
		Assert.assertNotNull(loaded);
		Assert.assertEquals(value, loaded.getValue());
	}
	
	@Test
	public void update() {
		String group = "001",
			   code = "code0",
			   value = "value0";
		Code code0 = newCode(group, code, value, "test user");
		codeMapper.create(code0);
		Code loaded = codeMapper.getCode(group, code);
		Assert.assertNotNull(loaded);
		Assert.assertEquals(value, loaded.getValue());
		
		value = "value zero";
		String description = "code0 description";
		
		code0.setValue(value);
		code0.setDescription(description);
		codeMapper.update(code0);
		
		loaded = codeMapper.getCode(group, code);
		Assert.assertNotNull(loaded);
		Assert.assertEquals(value, loaded.getValue());
		Assert.assertEquals(description, loaded.getDescription());
	}

	@Test
	public void delete() {
		String[] groups = {"001", "002", "003"};
		for (String group: groups) {
			codeMapper.create(newCode(group, group + "-code0", group + "-value0", "test user"));
			codeMapper.create(newCode(group, group + "-code1", group + "-value1", "test user"));
		}
		
		codeMapper.deleteCodes("001", "001-code0");
		Map<String, List<DataObject>> codes = codeMapper.getCodes(groups);
		List<DataObject> code001 = codes.get("001");
		Assert.assertEquals(1, code001.size());
		codeMapper.deleteCodes("001");
		codes = codeMapper.getCodes(groups);
		code001 = codes.get("001");
		Assert.assertNull(code001);
	}
	
	@After
	public void teardown() {
		codeMapper.deleteCodes("001");
		codeMapper.deleteCodes("002");
		codeMapper.deleteCodes("003");
	}
}
