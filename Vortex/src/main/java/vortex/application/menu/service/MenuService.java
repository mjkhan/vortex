package vortex.application.menu.service;

import java.util.List;

import vortex.application.ApplicationAccess;
import vortex.application.menu.Menu;
import vortex.support.data.DataObject;

public interface MenuService extends ApplicationAccess {
	public List<DataObject> search(String field, String value, int start, int fetch);
	
	public default List<DataObject> search(String field, String value) {
		return search(field, value, -1, 0);
	}
	
	public Menu getMenu(String id);
	
	public String create(Menu menu);
	
	public boolean update(Menu menu);
	
	public boolean move(String parentID, String... menuIDs);
	
	public boolean reorder(String parentID, String... menuIDs);
	
	public boolean reorder(String menuID, int offset);
	
	public boolean setStatus(String status, String... menuIDs);
	
	public boolean delete(String... menuIDs);
}