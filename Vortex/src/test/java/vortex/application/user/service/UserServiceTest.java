package vortex.application.user.service;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import vortex.application.VortexTest;

public class UserServiceTest extends VortexTest {
	private UserService userService = getBean("userService");
	
	private User newUser(int num) {
		User user = new User();
		user.setId("user-id" + num);
		user.setName("user name" + num);
		user.setAlias("alias" + num);
		return user;
	}

	@Test
	public void create() {
		User user = newUser(0);
		userService.create(dataObject().set("user", user));
		String id = user.getId();
		User loaded = userService.getUser(dataObject().set("userID", id)).value("user");
		Assert.assertNotNull(loaded);
		Assert.assertEquals(user.getId(), loaded.getId());
		Assert.assertEquals(user.getName(), loaded.getName());
		Assert.assertEquals(user.getAlias(), loaded.getAlias());
	}

	@Test
	public void update() {
		User user = newUser(0);
		userService.create(dataObject().set("user", user));
		String id = user.getId();
		User loaded = userService.getUser(dataObject().set("userID", id)).value("user");
		
		String name = "user name zero",
			   alias = "alias zero";
		loaded.setName(name);
		loaded.setAlias(alias);
		userService.update(dataObject().set("user", loaded));
		loaded = userService.getUser(dataObject().set("userID", id)).value("user");

		Assert.assertNotNull(loaded);
		Assert.assertEquals(id, loaded.getId());
		Assert.assertEquals(name, loaded.getName());
		Assert.assertEquals(alias, loaded.getAlias());
	}
	
	@Test
	public void search() {
		for (int i = 0; i < 5; ++i) {
			userService.create(dataObject().set("user", newUser(i)));
		}
		userService.search(dataObject());
		userService.search(dataObject().set("field", "USER_NAME").set("value", "user"));
		userService.search(dataObject().set("field", "USER_NAME").set("value", "user").set("status", "998"));
		userService.search(dataObject().set("field", "USER_NAME").set("value", "value"));

		userService.search(dataObject().set("start", 0).set("fetch", 10));
		userService.search(dataObject().set("field", "USER_NAME").set("value", "user").set("start", 0).set("fetch", 10));
		userService.search(dataObject().set("field", "USER_NAME").set("value", "user").set("status", "998").set("start", 0).set("fetch", 10));
		userService.search(dataObject().set("field", "USER_NAME").set("value", "value").set("start", 0).set("fetch", 10));
	}
	
	@Test
	public void setStatus() {
		ArrayList<String> userIDs = new ArrayList<>();
		for (int i = 0; i < 5; ++i) {
			User user = newUser(i);
			userService.create(dataObject().set("user", user));
			userIDs.add(user.getId());
		}
		
		String status = "998";
		userService.setStatus(dataObject().set("status", status).set("userID", userIDs.get(0) + "," + userIDs.get(1)));
		for (int i = 0; i < 2; ++i) {
			User user = userService.getUser(dataObject().set("userID", userIDs.get(i))).value("user");
			Assert.assertEquals(status, user.getStatus());
		}
		
		userService.remove(dataObject().set("userID", userIDs.get(2) + "," + userIDs.get(3) + "," + userIDs.get(4)));
		for (int i = 2; i < 5; ++i) {
			User user = userService.getUser(dataObject().set("userID", userIDs.get(i))).value("user");
			Assert.assertEquals("999", user.getStatus());
		}
	}
	
	@After
	public void tearDown() {
		userService.delete(dataObject());
	}

}