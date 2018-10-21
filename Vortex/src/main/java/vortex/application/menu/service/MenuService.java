package vortex.application.menu.service;

import java.util.List;

import vortex.application.menu.Menu;
import vortex.application.menu.MenuContext;
import vortex.support.data.DataObject;

public interface MenuService {
	public MenuContext getTree();
	
	public MenuContext getMenuContext();
	
	public List<DataObject> getMenus(String parentID);
	
	public Menu getMenu(String id);
	
	public boolean create(Menu menu);
	
	public boolean update(Menu menu);
	
	public boolean move(String parentID, String... menuIDs);
	
	public boolean reorder(String parentID, String... menuIDs);
	
	public boolean reorder(String parentID, String menuID, int offset);
	
	public boolean setStatus(String status, String... menuIDs);
	
	public boolean delete(String... menuIDs);
}