package vortex.application.group;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import vortex.application.VortexTest;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

public class GroupMapperTest extends VortexTest {
	private GroupMapper groupMapper = getBean("groupMapper");
	
	@Test
	public void search() {
		DataObject req = new DataObject().set("order", "GRP_ID");
		groupMapper.search(req);
		groupMapper.search(req.set("field", "GRP_NAME").set("value", "Test"));
		groupMapper.search(req.set("status", Status.ACTIVE.code()));
		groupMapper.search(req.set("start", 0).set("fetch", 10));
	}
	
	@Test
	public void createWithId() {
		String id = "001";
		String name = "테스트 그룹";
		Group group = new Group();
		group.setId(id);
		group.setName(name);
		String user = "test user";
		group.setCreatedBy(user);
		group.setModifiedBy(user);
		id = groupMapper.create(group);
		
		Group loaded = groupMapper.getGroup(id);
		Assert.assertEquals(name, loaded.getName());
		Assert.assertEquals(user, loaded.getCreatedBy());
		Assert.assertEquals(user, loaded.getModifiedBy());
	}
	
	@Test
	public void createWithoutId() {
		String name = "테스트 그룹";
		Group group = new Group();
		group.setName(name);
		String user = "test user";
		group.setCreatedBy(user);
		group.setModifiedBy(user);
		String id = groupMapper.create(group);
		
		Group loaded = groupMapper.getGroup(id);
		Assert.assertEquals(name, loaded.getName());
		Assert.assertEquals(user, loaded.getCreatedBy());
		Assert.assertEquals(user, loaded.getModifiedBy());
	}
	
	@Test
	public void update() {
		String name = "테스트 그룹";
		Group group = new Group();
		group.setName(name);
		String user = "test user";
		group.setCreatedBy(user);
		group.setModifiedBy(user);
		String id = groupMapper.create(group);
		
		Group loaded = groupMapper.getGroup(id);
		Assert.assertEquals(name, loaded.getName());
		Assert.assertEquals(user, loaded.getCreatedBy());
		Assert.assertEquals(user, loaded.getModifiedBy());
		
		name = "테스트 그룹(Test group)";
		loaded.setName(name);
		groupMapper.update(loaded);
		
		loaded = groupMapper.getGroup(id);
		Assert.assertEquals(name, loaded.getName());
	}
	
	@Test
	public void setStatus() {
		String user = "test user";

		Group group0 = new Group();
		group0.setName("테스트 그룹0");
		group0.setCreatedBy(user);
		group0.setModifiedBy(user);
		String id0 = groupMapper.create(group0);

		Group group1 = new Group();
		group1.setName("테스트 그룹0");
		group1.setCreatedBy(user);
		group1.setModifiedBy(user);
		String id1 = groupMapper.create(group1);
		
		groupMapper.setStatus(Status.INACTIVE.code(), id0);
		group0 = groupMapper.getGroup(id0);
		Assert.assertEquals(Status.INACTIVE, group0.status());

		groupMapper.setStatus(Status.ACTIVE.code(), id0);
		group0 = groupMapper.getGroup(id0);
		Assert.assertEquals(Status.ACTIVE, group0.status());
		
		groupMapper.setStatus(Status.INACTIVE.code(), id0, id1);
		group0 = groupMapper.getGroup(id0);
		group1 = groupMapper.getGroup(id1);
		Assert.assertEquals(Status.INACTIVE, group0.status());
		Assert.assertEquals(Status.INACTIVE, group1.status());
	}
	
	@Test
	public void remove() {
		String user = "test user";

		Group group0 = new Group();
		group0.setName("테스트 그룹0");
		group0.setCreatedBy(user);
		group0.setModifiedBy(user);
		String id0 = groupMapper.create(group0);

		Group group1 = new Group();
		group1.setName("테스트 그룹0");
		group1.setCreatedBy(user);
		group1.setModifiedBy(user);
		String id1 = groupMapper.create(group1);
		
		groupMapper.remove(id0, id1);
		
		group0 = groupMapper.getGroup(id0);
		group1 = groupMapper.getGroup(id1);
		Assert.assertEquals(Status.REMOVED, group0.status());
		Assert.assertEquals(Status.REMOVED, group1.status());
	}
	
	@Test
	public void addMembers() {
		String user = "test user";

		Group group0 = new Group();
		group0.setName("테스트 그룹0");
		group0.setCreatedBy(user);
		group0.setModifiedBy(user);
		String id0 = groupMapper.create(group0);

		Group group1 = new Group();
		group1.setName("테스트 그룹0");
		group1.setCreatedBy(user);
		group1.setModifiedBy(user);
		String id1 = groupMapper.create(group1);
		
		String[] groupIDs = {id0, id1};
		
		int saved = groupMapper.addMembers(user, groupIDs, "010", "001", "002", "003");
		Assert.assertEquals(6, saved);
		saved = groupMapper.addMembers(user, groupIDs, "010", "001", "002", "003");
		Assert.assertEquals(0, saved);
	}
	
	@Test
	public void deleteMembers() {
		String user = "test user";

		Group group0 = new Group();
		group0.setName("테스트 그룹0");
		group0.setCreatedBy(user);
		group0.setModifiedBy(user);
		String id0 = groupMapper.create(group0);

		Group group1 = new Group();
		group1.setName("테스트 그룹0");
		group1.setCreatedBy(user);
		group1.setModifiedBy(user);
		String id1 = groupMapper.create(group1);
		
		String[] groupIDs = {id0, id1};
		String memberType = "010",
			   member0 = "001",
			   member1 = "002",
			   member2 = "003";
		groupMapper.addMembers(user, groupIDs, memberType, member0, member1, member2);
		
		int saved = groupMapper.deleteMembers(id0, memberType, member0);
		Assert.assertEquals(1, saved);

		saved = groupMapper.deleteMembers(id0, memberType, member1, member2);
		Assert.assertEquals(2, saved);

		saved = groupMapper.deleteMembers(groupIDs, null);
		Assert.assertEquals(3, saved);
	}
	
	@Test
	public void reorderMembers() {
		String user = "test user";

		Group group0 = new Group();
		group0.setName("테스트 그룹0");
		group0.setCreatedBy(user);
		group0.setModifiedBy(user);
		String id0 = groupMapper.create(group0);

		Group group1 = new Group();
		group1.setName("테스트 그룹0");
		group1.setCreatedBy(user);
		group1.setModifiedBy(user);
		String id1 = groupMapper.create(group1);
		
		String[] groupIDs = {id0, id1};
		String memberType = "010",
			   member0 = "001",
			   member1 = "002",
			   member2 = "003";
		String[] memberIDs = {member2, member1};
		groupMapper.addMembers(user, groupIDs, memberType, member0, member1, member2);

		groupMapper.reorderMembers(id0, memberType, memberIDs);
		
	}

	@After
	public void clear() {
		groupMapper.deleteGroups();
	}
}