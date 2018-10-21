package vortex.application.menu;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import vortex.application.User;
import vortex.support.data.hierarchy.Hierarchy;

public class MenuContext extends Hierarchy<Menu> {
	private static final long serialVersionUID = 1L;
	private Map<String, List<String>> actionPermissions;
	private Map<String, Menu> byUrls;
	
	public Map<String, List<String>> getActionPermissions() {
		return actionPermissions;
	}
	
	public MenuContext setActionPermissions(Map<String, List<String>> actionPermissions) {
		this.actionPermissions = actionPermissions;
		return this;
	}
	
	public String getPermittedAction() {
		for (Menu menu: topElements()) {
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
	
	public Menu byUrl(String url) {
		return isEmpty(url) || isEmpty(byUrls) ? null : byUrls.get(url);
	}
	
	public MenuContext init() {
		byUrls = getElements().stream().filter(e -> !isEmpty(e.getActionPath()))
				.collect(Collectors.toMap(e -> e.getActionPath(), e -> e));
		return this;
	}
}