package vortex.application.menu.service;

import java.util.List;

import vortex.application.ApplicationAccess;
import vortex.application.menu.Menu;
import vortex.support.data.DataObject;
import vortex.support.data.hierarchy.Hierarchy;

public interface MenuService extends ApplicationAccess {
	public Hierarchy<Menu> getTree();
	
	public List<DataObject> getMenus(String parentID);
	
	public Menu getMenu(String id);
	
	public String create(Menu menu);
	
	public boolean update(Menu menu);
	
	public boolean move(String parentID, String... menuIDs);
	
	public boolean reorder(String parentID, String... menuIDs);
	
	public boolean reorder(String parentID, String menuID, int offset);
	
	public boolean setStatus(String status, String... menuIDs);
	
	public boolean delete(String... menuIDs);
}