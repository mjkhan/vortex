package vortex.application.menu.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.menu.Menu;
import vortex.support.data.DataObject;

@Service("menuService")
public class MenuServiceImpl extends ApplicationService implements MenuService {
	@Resource(name="menuMapper")
	private MenuMapper menuMapper;
	
	@Override
	public List<DataObject> search(String field, String value, int start, int fetch) {
		return menuMapper.search(field, value, start, fetch);
	}

	@Override
	public Menu getMenu(String id) {
		return menuMapper.getMenu(id);
	}

	@Override
	public String create(Menu menu) {
		return menuMapper.create(menu);
	}

	@Override
	public boolean update(Menu menu) {
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
	public boolean reorder(String menuID, int offset) {
		return menuMapper.reorder(menuID, offset);
	}

	@Override
	public boolean setStatus(String status, String... menuIDs) {
		return menuMapper.setStatus(status, menuIDs);
	}

	@Override
	public boolean delete(String... menuIDs) {
		return menuMapper.delete(menuIDs);
	}

}