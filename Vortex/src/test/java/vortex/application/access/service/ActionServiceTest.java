package vortex.application.access.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import vortex.application.VortexTest;
import vortex.application.access.service.Action;
import vortex.application.access.service.ActionMapper;
import vortex.support.data.DataObject;

public class ActionServiceTest extends VortexTest {
	private ActionMapper actionMapper = getBean("actionMapper");
	
	@Test
	public void getActions() {
		String groupID = "001";
		for (int i = 0; i < 3; ++i) {
			Action action = new Action();
			action.setId("action-" + i);
			action.setGroupID(groupID);
			action.setName("action name " + i);
			action.setModifiedBy("test user");
			actionMapper.create(action);
		}
		List<DataObject> list = actionMapper.getActions(groupID);
		Assert.assertEquals(3, list.size());
	}
	
	@Test
	public void create() {
		String id = "action-0",
			   name = "action zero",
			   groupID = "001";
		Action action = new Action();
		action.setId(id);
		action.setGroupID(groupID);
		action.setName(name);
		action.setModifiedBy("test user");
		actionMapper.create(action);
		
		Action loaded = actionMapper.getAction(id);
		Assert.assertNotNull(loaded);
		Assert.assertEquals(id, loaded.getId());
		Assert.assertEquals(name, loaded.getName());
		Assert.assertEquals(groupID, loaded.getGroupID());
	}
	
	@Test
	public void update() {
		String id = "action-0",
			   name = "action zero",
			   groupID = "001";
		Action action = new Action();
		action.setId(id);
		action.setGroupID(groupID);
		action.setName(name);
		action.setModifiedBy("test user");
		actionMapper.create(action);
		
		name = "Action Zero";
		action.setName(name);
		actionMapper.update(action);
		
		Action loaded = actionMapper.getAction(id);
		Assert.assertNotNull(loaded);
		Assert.assertEquals(id, loaded.getId());
		Assert.assertEquals(name, loaded.getName());
		Assert.assertEquals(groupID, loaded.getGroupID());
	}
	
	@Test
	public void delete() {
		String groupID = "001";
		for (int i = 0; i < 3; ++i) {
			Action action = new Action();
			action.setId("action-" + i);
			action.setGroupID(groupID);
			action.setName("action name " + i);
			action.setModifiedBy("test user");
			actionMapper.create(action);
		}
		int saved = actionMapper.deleteActions(groupID, "action-0", "action-1");
		Assert.assertEquals(2, saved);
		saved = actionMapper.deleteActions(groupID);
		Assert.assertEquals(1, saved);
	}

	@After
	public void tearDown() throws Exception {
		actionMapper.deleteActions("001");
	}
}