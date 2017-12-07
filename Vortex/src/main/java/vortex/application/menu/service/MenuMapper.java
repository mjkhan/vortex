package vortex.application.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import vortex.application.menu.Menu;
import vortex.support.data.DataObject;
import vortex.support.data.Status;
import vortex.support.data.hierarchy.Hierarchy;
import vortex.support.data.hierarchy.HierarchyBuilder;
import vortex.support.database.AbstractMapper;

@Repository("menuMapper")
public class MenuMapper extends AbstractMapper {
	public Hierarchy<Menu> getTree() {
		List<Menu> menus = selectList("menu.getTree");
		return new HierarchyBuilder<Menu>().setElements(menus).build();
	}
	
	public List<DataObject> getMenus(String parentID) {
		return selectList("menu.getMenus", parentID);
	}
	
	public Menu getMenu(String id) {
		return selectOne("menu.getMenu", id);
	}
	
	private String newId() {
		return selectOne("menu.newID");
	}
	
	public String create(Menu menu) {
		String id = newId();
		menu.setId(id);
		insert("menu.insert", menu);
		return id;
	}
	
	public boolean update(Menu menu) {
		return update("menu.update", menu) == 1;
	}
	
	public boolean move(String parentID, String... menuIDs) {
		if (isEmpty(menuIDs)) return false;
		return update(
			"menu.move",
			params()
				.set("parentID", parentID)
				.set("menuIDs", menuIDs)
		) > 0;
	}
	
	public boolean reorder(String parentID, String... menuIDs) {
		if (isEmpty(menuIDs)) return false;
		return update(
			"menu.reorder",
			params()
				.set("parentID", parentID)
				.set("menuIDs", menuIDs)
		) > 0;
	}
	
	public boolean reorder(String parentID, String menuID, int offset) {
		if (offset == 0) return false;
		List<DataObject> menus = getMenus(parentID);
		if (menus.isEmpty()) return false;

		DataObject menuInfo = menus.stream().filter(row -> menuID.equals(row.get("menu_id"))).findFirst().get();
		int index = !isEmpty(menuInfo) ? menus.indexOf(menuInfo) : -1;
		if (index < 0) return false;
		
		int target = Math.min(Math.max(0, index + offset), menus.size() - 1);
		if (index == target) return false;

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
	
	public boolean setStatus(String modifiedBy, String status, String... menuIDs) {
		String[] IDs = getAllIDs(menuIDs);
		DataObject params = params()
			.set("menuIDs", IDs)
			.set("status", status)
			.set("modifiedBy", modifiedBy);
		int affected = update("menu.setStatus", params);
		return affected > 0;
	}
	
	public boolean delete(String modifiedBy, String... menuIDs) {
		return setStatus(modifiedBy, Status.REMOVED.code(), menuIDs);
	}
}