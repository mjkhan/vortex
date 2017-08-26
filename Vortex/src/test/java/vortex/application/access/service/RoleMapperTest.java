package vortex.application.access.service;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import vortex.application.VortexTest;
import vortex.application.access.service.Role;
import vortex.application.access.service.RoleMapper;

public class RoleMapperTest extends VortexTest {
	private RoleMapper roleMapper = getBean("roleMapper");

	private Role newRole(String name) {
		String userID = "test user";
		Role role = new Role();
		role.setName(name);
		role.setModifiedBy(userID);
		return role;
	}

	@Test
	public void create() {
		String name	= "role 0";
		Role role = newRole(name);
		String id = roleMapper.create(role);
		
		Role loaded = roleMapper.getRole(id);
		Assert.assertNotNull(loaded);
		Assert.assertEquals(id, loaded.getId());
		Assert.assertEquals(name, loaded.getName());
	}
	
	@Test
	public void update() {
		String name	= "role 0";
		Role role = newRole(name);
		String id = roleMapper.create(role);
		
		Role loaded = roleMapper.getRole(id);
		Assert.assertNotNull(loaded);
		Assert.assertEquals(id, loaded.getId());
		Assert.assertEquals(name, loaded.getName());
		
		name = "role zero";
		String description = "test role";
		role.setName(name);
		role.setDescription(description);
		roleMapper.update(role);
		
		loaded = roleMapper.getRole(id);
		Assert.assertEquals(name, loaded.getName());
		Assert.assertEquals(description, loaded.getDescription());
	}
	
	@Test
	public void remove() {
		ArrayList<String> roleIDs = new ArrayList<>();
		for (int i = 0; i < 5; ++i)
			roleIDs.add(roleMapper.create(newRole("role " + i)));
		
		String id = roleIDs.get(0);
		Assert.assertEquals(1, roleMapper.remove(id));
		Assert.assertNull(roleMapper.getRole(id));
		
		Assert.assertEquals(2, roleMapper.remove(new String[]{roleIDs.get(1), roleIDs.get(2)}));
		Assert.assertNull(roleMapper.getRole(roleIDs.get(1)));
		Assert.assertNull(roleMapper.getRole(roleIDs.get(2)));
		
		Assert.assertEquals(2, roleMapper.remove());
		Assert.assertNull(roleMapper.getRole(roleIDs.get(3)));
		Assert.assertNull(roleMapper.getRole(roleIDs.get(4)));
	}

	@After
	public void tearDown() {
		roleMapper.remove();
	}
}