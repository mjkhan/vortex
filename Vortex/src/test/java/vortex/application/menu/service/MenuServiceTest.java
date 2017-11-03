package vortex.application.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import vortex.application.VortexTest;
import vortex.application.menu.Menu;
import vortex.support.data.DataObject;
import vortex.support.data.Status;
import vortex.support.data.hierarchy.Hierarchy;
import vortex.support.data.hierarchy.Stringify;

public class MenuServiceTest extends VortexTest {
	private MenuService menuService = getBean("menuService");
	
	private Menu newMenu(String name, String action, String parentID) {
		Menu menu = new Menu();
		menu.setName(name);
		menu.setActionPath(action);
		menu.setParentID(parentID);
		return menu;
	}
	
	private Menu create(String name, String action, String parentID) {
		String id = menuService.create(newMenu(name, action, parentID));
		return getMenu(id);
	}
	
	private Menu getMenu(String id) {
		return menuService.getMenu(id);
	}
	
	@Test
	public void createUpdateGet() {
		String name = "test menu",
			   action = "test/menu";
		Menu menu = create(name, action, null);
		
		Assert.assertNotNull(menu);
		Assert.assertEquals(name, menu.getName());
		Assert.assertEquals(action, menu.getActionPath());
		Assert.assertEquals(Menu.ROOT_ID, menu.getParentID());
		Assert.assertEquals(Status.ACTIVE, menu.status());
		
		name = "test 메뉴";
		action = "test/MENU";
		menu.setName(name);
		menu.setActionPath(action);
		
		boolean saved = menuService.update(menu);
		Assert.assertTrue(saved);
		menu = getMenu(menu.getId());
		Assert.assertEquals(name, menu.getName());
		Assert.assertEquals(action, menu.getActionPath());
	}
	
	@Test
	public void getMenus() {
		Menu parent0 = create("parent0", null, null),
			 parent1 = create("parent1", null, null),
			 child0_0 = create("child0-0", null, parent0.getId()),
			 child0_1 = create("child0-1", null, parent0.getId()),
			 child1_0 = create("child1-0", null, parent1.getId()),
			 child1_1 = create("child1-1", null, parent1.getId());
		
		Consumer<Menu> assertChildren = menu -> {
			List<DataObject> children = menuService.getMenus(menu.getId());
			Assert.assertEquals(2, children.size());
			children.forEach(child -> menu.getId().equals(child.get("prnt_id")));
		};
		
		assertChildren.accept(parent0);
		assertChildren.accept(parent1);
	}
	
	@Test
	public void move() {
		Menu parent0 = create("parent0", null, null),
			 parent1 = create("parent1", null, null),
			 child0_0 = create("child0-0", null, parent0.getId()),
			 child0_1 = create("child0-1", null, parent0.getId()),
			 child1_0 = create("child1-0", null, parent1.getId()),
			 child1_1 = create("child1-1", null, parent1.getId());
		
		String parentID = parent1.getId();
		String[] menuIDs = {child0_0.getId(), child0_1.getId()};
		boolean moved = menuService.move(parentID, menuIDs);
		
		Assert.assertTrue(moved);
		List<DataObject> children = menuService.getMenus(parent0.getId());
		Assert.assertTrue(children.isEmpty());
		
		children = menuService.getMenus(parent1.getId());
		Assert.assertEquals(4, children.size());
	}
	
	@Test
	public void reorder() {
		Menu parent0 = create("parent0", null, null),
			 parent1 = create("parent1", null, null),
			 child0_0 = create("child0-0", null, parent0.getId()),
			 child0_1 = create("child0-1", null, parent0.getId()),
			 child0_2 = create("child0-2", null, parent0.getId()),
			 child1_0 = create("child1-0", null, parent1.getId()),
			 child1_1 = create("child1-1", null, parent1.getId()),
			 child1_2 = create("child1-2", null, parent1.getId());
		
		String parentID = parent0.getId();
		String[] menuIDs = {child0_2.getId(), child0_1.getId()};
		boolean reordered = menuService.reorder(parentID, menuIDs);
		Assert.assertTrue(reordered);
		
		parentID = parent1.getId();
		String menuID = child1_2.getId();
		reordered = menuService.reorder(parentID, menuID, 0);
		Assert.assertFalse(reordered);
		List<DataObject> children = menuService.getMenus(parentID);
		Assert.assertEquals(menuID, children.get(2).get("menu_id"));

		reordered = menuService.reorder(parentID, menuID, 1);
		Assert.assertFalse(reordered);
		children = menuService.getMenus(parentID);
		Assert.assertEquals(menuID, children.get(2).get("menu_id"));

		reordered = menuService.reorder(parentID, menuID, 10);
		Assert.assertFalse(reordered);
		children = menuService.getMenus(parentID);
		Assert.assertEquals(menuID, children.get(2).get("menu_id"));

		reordered = menuService.reorder(parentID, menuID, -1);
		Assert.assertTrue(reordered);
		children = menuService.getMenus(parentID);
		Assert.assertEquals(menuID, children.get(1).get("menu_id"));

		reordered = menuService.reorder(parentID, menuID, -10);
		Assert.assertTrue(reordered);
		children = menuService.getMenus(parentID);
		Assert.assertEquals(menuID, children.get(0).get("menu_id"));

		reordered = menuService.reorder(parentID, menuID, -10);
		Assert.assertFalse(reordered);
		children = menuService.getMenus(parentID);
		Assert.assertEquals(menuID, children.get(0).get("menu_id"));

		reordered = menuService.reorder(parentID, menuID, 10);
		Assert.assertTrue(reordered);
		children = menuService.getMenus(parentID);
		Assert.assertEquals(menuID, children.get(2).get("menu_id"));
	}
	
	@Test
	public void getTree() {
		Menu parent0 = create("parent0", null, null),
			 parent1 = create("parent1", null, null),
			 child0_0 = create("child0-0", null, parent0.getId()),
			 child0_1 = create("child0-1", null, parent0.getId()),
			 child0_2 = create("child0-2", null, parent0.getId()),
			 child1_0 = create("child1-0", null, parent1.getId()),
			 child1_1 = create("child1-1", null, parent1.getId()),
			 child1_2 = create("child1-2", null, parent1.getId());
		
		Hierarchy<Menu> tree = menuService.getTree();
		System.out.println(Menu.Support.toString(tree.topElements()));
	}
	
	@Test
	public void setStatus() {
		
	}
	
	@Test
	public void convert() {
		Function<String, Menu> newMenu = (id) -> {
			Menu menu = new Menu();
			menu.setId(id);
			menu.setName("menu " + id);
			menu.setActionPath("/info?get=");
			return menu;
		};
		Menu parent0 = newMenu.apply("00000"),
			 parent1 = newMenu.apply("10000");
		ArrayList<Menu> menus = new ArrayList<>();
		menus.add(parent0);
		menus.add(parent1);
		parent0.add(newMenu.apply("00001"));
		parent0.add(newMenu.apply("00002"));
		parent0.add(newMenu.apply("00003"));
		parent0.getChildren().get(1).add(newMenu.apply("00021"));
		parent1.add(newMenu.apply("10001"));
		parent1.add(newMenu.apply("10002"));
		parent1.add(newMenu.apply("10003"));
		parent1.getChildren().get(1).add(newMenu.apply("10021"));
		System.out.println(Menu.Support.toString(menus));

		System.out.println(
			new Stringify<Menu>()
				.beginElement((e, level) -> Stringify.indent("    ", level) + "<li id=\"" + e.getId() + "\"><a href=\"" + e.getAction() + e.getId() + "\">" + e.getName() + "</a>")
				.endElement((e, level) -> "\n" + Stringify.indent("    ", level) + "</li>\n")
				.beginChildren((e, level) -> "\n" + Stringify.indent("    ", level) + "<ul>\n")
				.endChildren((e, level) -> Stringify.indent("    ", level) + "</ul>")
				.toString(menus)
		);
	}

	@After
	public void teardown() {
	}
}