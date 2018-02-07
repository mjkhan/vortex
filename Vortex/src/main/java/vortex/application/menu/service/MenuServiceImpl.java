package vortex.application.menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.menu.Menu;
import vortex.application.menu.MenuContext;
import vortex.support.data.DataObject;
import vortex.support.data.hierarchy.Hierarchy;

@Service("menuService")
public class MenuServiceImpl extends ApplicationService implements MenuService {
	@Autowired
	private MenuMapper menuMapper;

	@Override
	public Hierarchy<Menu> getTree() {
		return menuMapper.getTree();
	}

	@Cacheable(value="menuContext", key="#root.methodName")
	@Override
	public MenuContext getMenuContext() {
		log().debug(() -> "Loading menuContext...");
		return new MenuContext()
			.setMenus(getTree())
			.setActionPermissions(menuMapper.getMenuActionPermissions());
	}
	
	@Override
	public List<DataObject> getMenus(String field) {
		return menuMapper.getMenus(field);
	}

	@Override
	public Menu getMenu(String id) {
		return menuMapper.getMenu(id);
	}

	@Override
	public boolean create(Menu menu) {
		return menuMapper.create(menu);
	}

	@Override
	public boolean update(Menu menu) {
		return menuMapper.update(menu);
	}

	@Override
	public boolean move(String parentID, String... menuIDs) {
		return menuMapper.move(parentID, menuIDs) > 0;
	}

	@Override
	public boolean reorder(String parentID, String... menuIDs) {
		return menuMapper.reorder(parentID, menuIDs) > 0;
	}

	@Override
	public boolean reorder(String parentID, String menuID, int offset) {
		return menuMapper.reorder(parentID, menuID, offset) > 0;
	}

	@Override
	public boolean setStatus(String status, String... menuIDs) {
		return menuMapper.setStatus(status, menuIDs) > 0;
	}

	@Override
	public boolean delete(String... menuIDs) {
		return menuMapper.delete(menuIDs) > 0;
	}
}