package vortex.application.menu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import vortex.application.DataMapper;
import vortex.application.menu.Menu;
import vortex.support.data.DataObject;
import vortex.support.data.Status;
import vortex.support.data.hierarchy.Hierarchy;
import vortex.support.data.hierarchy.HierarchyBuilder;

@Repository("menuMapper")
public class MenuMapper extends DataMapper {
	public Hierarchy<Menu> getTree() {
		List<Menu> menus = selectList("menu.getTree");
		return new HierarchyBuilder<Menu>().setElements(menus).build();
	}
	
	public Map<String, List<String>> getMenuActionRoles() {
		List<Map<String, Object>> list = selectList("roleMember.getMenuActionRoles");
		Map<String, List<String>> rolesByAction = new HashMap<>();
		list.forEach(row -> 
			rolesByAction
				.computeIfAbsent((String)row.get("ACT_PATH"), key -> new ArrayList<>())
				.add((String)row.get("ROLE_ID"))
		);
		return rolesByAction;
	}
	
	public List<DataObject> getMenus(String parentID) {
		return selectList("menu.getMenus", parentID);
	}
	
	public Menu getMenu(String id) {
		return selectOne("menu.getMenu", id);
	}

	public boolean create(Menu menu) {
		return menu != null ? insert("menu.insert", params(true).set("menu", menu)) == 1 : false;
	}
	
	public boolean update(Menu menu) {
		return menu != null ? update("menu.update", params(true).set("menu", menu)) == 1 : false;
	}
	
	public int move(String parentID, String... menuIDs) {
		if (isEmpty(menuIDs)) return 0;
		return update(
			"menu.move",
			params()
				.set("parentID", parentID)
				.set("menuIDs", menuIDs)
		);
	}
	
	public int reorder(String parentID, String... menuIDs) {
		if (isEmpty(menuIDs)) return 0;
		return update(
			"menu.reorder",
			params(true)
				.set("parentID", parentID)
				.set("menuIDs", menuIDs)
		);
	}
	
	public int reorder(String parentID, String menuID, int offset) {
		if (offset == 0) return 0;
		List<DataObject> menus = getMenus(parentID);
		if (menus.isEmpty()) return 0;

		DataObject menuInfo = menus.stream().filter(row -> menuID.equals(row.get("menu_id"))).findFirst().get();
		int index = !isEmpty(menuInfo) ? menus.indexOf(menuInfo) : -1;
		if (index < 0) return 0;
		
		int target = Math.min(Math.max(0, index + offset), menus.size() - 1);
		if (index == target) return 0;

//		Collections.swap(menus, index, target);
		menus.add(target, menus.remove(index));
		
		List<String> menuIDs = menus.stream().map(row -> row.string("menu_id")).collect(Collectors.toList());
		return reorder(parentID, menuIDs.toArray(new String[menuIDs.size()]));
	}
	
	private String[] getAllIDs(String... menuIDs) {
		Hierarchy<Menu> tree = getTree();
		Map<String, Menu> index = tree.getIndex();
		ArrayList<Menu> menus = new ArrayList<>();
		for (String menuID: menuIDs) {
			Menu menu = index.get(menuID);
			if (menu == null || menus.contains(menu)) continue;
			menus.add(menu);
		}
		List<String> IDs = Menu.Support.getIDs(menus);
		return IDs.toArray(new String[IDs.size()]);
	}
	
	public int setStatus(String status, String... menuIDs) {
		return update(
			"menu.setStatus"
			,params(true)
			.set("menuIDs", getAllIDs(menuIDs))
			.set("status", status)
		);
	}
	
	public int delete(String... menuIDs) {
		return setStatus(Status.REMOVED.code(), menuIDs);
	}
}