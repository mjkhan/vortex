package vortex.application.menu.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import vortex.application.menu.Menu;
import vortex.support.data.DataObject;
import vortex.support.data.Status;
import vortex.support.database.AbstractMapper;

@Repository("menuMapper")
public class MenuMapper extends AbstractMapper {
	public List<DataObject> search(String field, String value, int start, int fetch) {
		return null;
	}
	
	public Menu getMenu(String id) {
		return selectOne("menu.getMenu", id);
	}
	
	private String newId() {
		return null;
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
		DataObject params = params().set("parentId", parentID).set("menuIDs", menuIDs);
		int affected = update("menu.move", params);
		return affected > 0;
	}
	
	public boolean reorder(String parentID, String... menuIDs) {
		DataObject params = params().set("parentId", parentID).set("menuIDs", menuIDs);
		int affected = update("menu.reorder", params);
		return affected > 0;
	}
	
	public boolean reorder(String menuID, int offset) {
		DataObject params = params().set("menuId", menuID).set("offset", offset);
		int affected = update("menu.reorderByOffset", params);
		return affected > 0;
	}
	
	public boolean setStatus(String status, String... menuIDs) {
		return false;
	}
	
	public boolean delete(String... menuIDs) {
		return setStatus(Status.REMOVED.code(), menuIDs);
	}
}