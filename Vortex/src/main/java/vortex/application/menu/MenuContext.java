package vortex.application.menu;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import vortex.application.User;
import vortex.support.AbstractObject;
import vortex.support.data.hierarchy.Hierarchy;

public class MenuContext extends AbstractObject {
	private Hierarchy<Menu> menus;
	private Map<String, List<String>> actionRoles;
	
	public Hierarchy<Menu> getMenus() {
		return menus;
	}

	public MenuContext setMenus(Hierarchy<Menu> menus) {
		this.menus = menus;
		return this;
	}
	
	public Map<String, List<String>> getActionRoles() {
		return actionRoles;
	}
	
	public MenuContext setActionRoles(Map<String, List<String>> actionRoles) {
		this.actionRoles = actionRoles;
		return this;
	}

	public String getPermittedAction(Menu menu) {
		String actionPath = menu.getActionPath();
		if (!isEmpty(actionPath)) {
			List<String> roles = ifEmpty(actionRoles.get(actionPath), () -> Collections.emptyList());
			List<String> userRoles = User.current().getRoleIDs();
			boolean permitted = !Collections.disjoint(roles, userRoles);
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
}