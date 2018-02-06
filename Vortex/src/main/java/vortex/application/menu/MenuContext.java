package vortex.application.menu;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import vortex.application.User;
import vortex.support.AbstractObject;
import vortex.support.data.hierarchy.Hierarchy;

public class MenuContext extends AbstractObject {
	private Hierarchy<Menu> menus;
	private Map<String, List<String>> actionPermissions;
	
	public Hierarchy<Menu> getMenus() {
		return menus;
	}

	public MenuContext setMenus(Hierarchy<Menu> menus) {
		this.menus = menus;
		return this;
	}
	
	public Map<String, List<String>> getActionPermissions() {
		return actionPermissions;
	}
	
	public MenuContext setActionPermissions(Map<String, List<String>> actionPermissions) {
		this.actionPermissions = actionPermissions;
		return this;
	}
	
	public String getPermittedAction() {
		Iterable<Menu> topMenus = menus.topElements();
		for (Menu menu: topMenus) {
			String actionPath = getPermittedAction(menu);
			if (!isEmpty(actionPath))
				return actionPath;
		}
		return null;
	}

	public String getPermittedAction(Menu menu) {
		String actionPath = menu.getActionPath();
		if (!isEmpty(actionPath)) {
			List<String>
				permissions = ifEmpty(actionPermissions.get(actionPath), () -> Collections.emptyList()),
				userPermissions = User.current().getPermissionIDs();
			boolean permitted = !Collections.disjoint(permissions, userPermissions);
			return permitted ? actionPath : null;
		} else {
			for (Menu child: menu.getChildren()) {
				String permittedAction = getPermittedAction(child);
				if (!isEmpty(permittedAction))
					return permittedAction;
			}
			return null;
		}
	}
	
	public boolean hasAction(List<Menu> menus, String action) {
		for (Menu menu: menus) {
			if (action.equals(menu.getActionPath()))
				return true;
			if (hasAction(menu.getChildren(), action))
				return true;
		}
		return false;
	}
}