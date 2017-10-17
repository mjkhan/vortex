package vortex.application.menu.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import vortex.application.menu.Menu;
import vortex.support.data.DataObject;
import vortex.support.data.Status;
import vortex.support.database.AbstractMapper;

@Repository("menuMapper")
public class MenuMapper extends AbstractMapper {
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
		int affected = update("menu.update", menu);
		return affected == 1;
	}
	
	public boolean move(String parentID, String... menuIDs) {
		DataObject params = params().set("parentID", parentID).set("menuIDs", menuIDs);
		int affected = update("menu.move", params);
		return affected > 0;
	}
	
	public boolean reorder(String parentID, String... menuIDs) {
		DataObject params = params().set("parentID", parentID).set("menuIDs", menuIDs);
		int affected = update("menu.reorder", params);
		return affected > 0;
	}
	
	public boolean reorder(String parentID, String menuID, int offset) {
		if (offset == 0) return false;
		List<DataObject> menus = getMenus(parentID);
		if (menus.isEmpty()) return false;

		DataObject menuInfo = menus.stream().filter(row -> menuID.equals(row.get("menu_id"))).findFirst().get();
		int index = !isEmpty(menuInfo) ? menus.indexOf(menuInfo) : -1;
		if (index < 0) return false;
		
		int target = Math.min(Math.max(0, index + offset), menus.size() - 1);
		Collections.swap(menus, index, target);
		
		List<String> menuIDs = menus.stream().map(row -> row.string("menu_id")).collect(Collectors.toList());
		return reorder(parentID, menuIDs.toArray(new String[menuIDs.size()]));
	}
	
	public boolean setStatus(String modifiedBy, String status, String... menuIDs) {
		DataObject params = params()
			.set("menuIDs", menuIDs)
			.set("status", status)
			.set("modifiedBy", modifiedBy);
		int affected = update("menu.reorderByOffset", params);
		return affected > 0;
	}
	
	public boolean delete(String modifiedBy, String... menuIDs) {
		return setStatus(modifiedBy, Status.REMOVED.code(), menuIDs);
	}
}