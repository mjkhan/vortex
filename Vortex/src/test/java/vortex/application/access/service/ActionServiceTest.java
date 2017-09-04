package vortex.application.access.service;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import vortex.application.VortexTest;
import vortex.application.group.Group;
import vortex.support.data.DataObject;

public class ActionServiceTest extends VortexTest {
	private ActionService actionService = getBean("actionService");
	
	private Group newGroup(int num) {
		Group group = new Group();
		group.setName("action group " + num);
//		String userID = "test user";
//		group.setCreatedBy(userID);
//		group.setModifiedBy(userID);
		return group;
	}
	
	private Action newAction(String groupID, int num) {
		Action action = new Action();
		action.setId("action-" + num);
		action.setGroupID(groupID);
		action.setName("action name " + num);
		action.setModifiedBy("test user");
		return action;
	}
	
	@Test
	public void getGroups() {}
	
	@Test
	public void getGroup() {}
	
	@Test
	public void createGroup() {
		String groupID = actionService.createGroup(dataObject().set("group", newGroup(0))).string("groupID");
		Group group = actionService.getGroup(dataObject().set("groupID", groupID)).value("group");
		Assert.assertNotNull(group);
	}
	
	@Test
	public void updateGroup() {
		String groupID = actionService.createGroup(dataObject().set("group", newGroup(0))).string("groupID");
		Group group = actionService.getGroup(dataObject().set("groupID", groupID)).value("group");
		Assert.assertNotNull(group);
		
		String name = "new name";
		group.setName(name);
		actionService.updateGroup(dataObject().set("group", group));
		group = actionService.getGroup(dataObject().set("groupID", groupID)).value("group");
		Assert.assertNotNull(group);
		Assert.assertEquals(name, group.getName());
	}
	
	@Test
	public void deleteGroups() {}
	
	@Test
	public void getActions() {
		String groupID = "001";
		for (int i = 0; i < 3; ++i) {
			Action action = newAction(groupID, i);
			actionService.createAction(dataObject().set("action", action));
		}
		List<DataObject> list = actionService.getActions(dataObject().set("groupID", groupID)).value("actions");
		Assert.assertEquals(3, list.size());
	}

	@Test
	public void createAction() {
		String groupID = "001";
		Action action = newAction(groupID, 0);
		actionService.createAction(dataObject().set("action", action));
		String id = action.getId(),
			   name = action.getName();
		
		Action loaded = actionService.getAction(dataObject().set("actionID", id)).value("action");
		Assert.assertNotNull(loaded);
		Assert.assertEquals(id, loaded.getId());
		Assert.assertEquals(name, loaded.getName());
		Assert.assertEquals(groupID, loaded.getGroupID());
	}
	
	@Test
	public void updateAction() {
		String groupID = "001";
		Action action = newAction(groupID, 0);
		actionService.createAction(dataObject().set("action", action));
		String id = action.getId(),
			   name = action.getName();
		
		Action loaded = actionService.getAction(dataObject().set("actionID", id)).value("action");
		
		name = "Action Zero";
		action.setName(name);
		actionService.updateAction(dataObject().set("action", action));
		
		loaded = actionService.getAction(dataObject().set("actionID", id)).value("action");

		Assert.assertNotNull(loaded);
		Assert.assertEquals(id, loaded.getId());
		Assert.assertEquals(name, loaded.getName());
		Assert.assertEquals(groupID, loaded.getGroupID());
	}
	
	@Test
	public void deleteActions() {
		String groupID = "001";
		for (int i = 0; i < 3; ++i) {
			actionService.createAction(dataObject().set("action", newAction(groupID, i)));
		}
		boolean saved = actionService.deleteActions(dataObject().set("actionID", "action-0,action-1")).bool("saved");
		Assert.assertEquals(true, saved);
		saved = actionService.deleteActions(dataObject()).bool("saved");
		Assert.assertEquals(true, saved);
		
		List<DataObject> list = actionService.getActions(dataObject().set("groupID", groupID)).value("actions");
		Assert.assertTrue(list.isEmpty());
	}

	@After
	public void tearDown() throws Exception {
		actionService.deleteGroups(dataObject());
	}
}