package vortex.application.menu.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.menu.Menu;
import vortex.support.data.DataObject;
import vortex.support.data.hierarchy.Hierarchy;

@Service("menuService")
public class MenuServiceImpl extends ApplicationService implements MenuService {
	@Resource(name="menuMapper")
	private MenuMapper menuMapper;

	@Override
	public Hierarchy<Menu> getTree() {
		return menuMapper.getTree();
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
	public String create(Menu menu) {
		menu.setModifiedBy(currentUser().getId());
		return menuMapper.create(menu);
	}

	@Override
	public boolean update(Menu menu) {
		menu.setModifiedBy(currentUser().getId());
		return menuMapper.update(menu);
	}

	@Override
	public boolean move(String parentID, String... menuIDs) {
		return menuMapper.move(parentID, menuIDs);
	}

	@Override
	public boolean reorder(String parentID, String... menuIDs) {
		return menuMapper.reorder(parentID, menuIDs);
	}

	@Override
	public boolean reorder(String parentID, String menuID, int offset) {
		return menuMapper.reorder(parentID, menuID, offset);
	}

	@Override
	public boolean setStatus(String status, String... menuIDs) {
		return menuMapper.setStatus(currentUser().getId(), status, menuIDs);
	}

	@Override
	public boolean delete(String... menuIDs) {
		return menuMapper.delete(currentUser().getId(), menuIDs);
	}
}