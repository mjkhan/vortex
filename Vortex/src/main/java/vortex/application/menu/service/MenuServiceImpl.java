package vortex.application.menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.menu.Menu;
import vortex.application.menu.MenuContext;
import vortex.support.data.DataObject;

@Service("menuService")
public class MenuServiceImpl extends ApplicationService implements MenuService {
	@Autowired
	private MenuMapper menuMapper;

	@Override
	public MenuContext getTree() {
		return menuMapper.getTree();
	}

	@Cacheable(value="menuContext", key="#root.methodName")
	@Override
	public MenuContext getMenuContext() {
		log().debug(() -> "(Re)loading menuContext...");
		return getTree()
			.setActionPermissions(menuMapper.getMenuActionPermissions())
			.init();
	}
	
	@Override
	public List<DataObject> getMenus(String field) {
		return menuMapper.getMenus(field);
	}

	@Override
	public Menu getMenu(String id) {
		return menuMapper.getMenu(id);
	}

	@CacheEvict(value="menuContext", allEntries=true)
	@Override
	public boolean create(Menu menu) {
		return menuMapper.create(menu);
	}

	@CacheEvict(value="menuContext", allEntries=true)
	@Override
	public boolean update(Menu menu) {
		return menuMapper.update(menu);
	}

	@CacheEvict(value="menuContext", allEntries=true)
	@Override
	public boolean move(String parentID, String... menuIDs) {
		return menuMapper.move(parentID, menuIDs) > 0;
	}

	@CacheEvict(value="menuContext", allEntries=true)
	@Override
	public boolean reorder(String parentID, String... menuIDs) {
		return menuMapper.reorder(parentID, menuIDs) > 0;
	}

	@CacheEvict(value="menuContext", allEntries=true)
	@Override
	public boolean reorder(String parentID, String menuID, int offset) {
		return menuMapper.reorder(parentID, menuID, offset) > 0;
	}

	@CacheEvict(value="menuContext", allEntries=true)
	@Override
	public boolean setStatus(String status, String... menuIDs) {
		return menuMapper.setStatus(status, menuIDs) > 0;
	}

	@CacheEvict(value="menuContext", allEntries=true)
	@Override
	public boolean delete(String... menuIDs) {
		return menuMapper.delete(menuIDs) > 0;
	}
}