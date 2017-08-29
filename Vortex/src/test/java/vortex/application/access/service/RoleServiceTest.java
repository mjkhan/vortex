package vortex.application.access.service;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import vortex.application.VortexTest;

public class RoleServiceTest extends VortexTest {
	private RoleService roleService = getBean("roleService");

	private Role newRole(String name) {
		String userID = "test user";
		Role role = new Role();
		role.setId(name);
		role.setName(name);
		role.setModifiedBy(userID);
		return role;
	}

	@Test
	public void create() {
		String name	= "role 0";
		Role role = newRole(name);
		String id = roleService.create(dataObject().set("role", role)).string("roleID");
		
		Role loaded = roleService.getRole(dataObject().set("roleID", id)).value("role");
		Assert.assertNotNull(loaded);
		Assert.assertEquals(id, loaded.getId());
		Assert.assertEquals(name, loaded.getName());
	}

	@Test
	public void update() {
		String name	= "role 0";
		Role role = newRole(name);
		String id = roleService.create(dataObject().set("role", role)).string("roleID");
		
		Role loaded = roleService.getRole(dataObject().set("roleID", id)).value("role");
		Assert.assertNotNull(loaded);
		Assert.assertEquals(id, loaded.getId());
		Assert.assertEquals(name, loaded.getName());
		
		name = "role zero";
		String description = "test role";
		role.setName(name);
		role.setDescription(description);
		roleService.update(dataObject().set("role", role));
		
		loaded = roleService.getRole(dataObject().set("roleID", id)).value("role");
		Assert.assertEquals(name, loaded.getName());
		Assert.assertEquals(description, loaded.getDescription());
	}
	
	@Test
	public void remove() {
		ArrayList<String> roleIDs = new ArrayList<>();
		for (int i = 0; i < 5; ++i)
			roleIDs.add(roleService.create(dataObject().set("role", newRole("role " + i))).string("roleID"));
		
		String id = roleIDs.get(0);
		roleService.delete(dataObject().set("roleID", id));
		Assert.assertNull(roleService.getRole(dataObject().set("roleID", id)).value("role"));
		
		roleService.delete(dataObject().set("roleID", roleIDs.get(1) + "," + roleIDs.get(2)));
		Assert.assertNull(roleService.getRole(dataObject().set("roleID", roleIDs.get(1))).value("role"));
		Assert.assertNull(roleService.getRole(dataObject().set("roleID", roleIDs.get(2))).value("role"));
		
		roleService.delete(dataObject());
		Assert.assertNull(roleService.getRole(dataObject().set("roleID", roleIDs.get(3))).value("role"));
		Assert.assertNull(roleService.getRole(dataObject().set("roleID", roleIDs.get(4))).value("role"));
	}

	@After
	public void tearDown() {
		roleService.delete(dataObject());
	}

}