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
	
	public void setMenus(Hierarchy<Menu> menus) {
		this.menus = menus;
	}
	
	public Map<String, List<String>> getActionRoles() {
		return actionRoles;
	}
	
	public void setActionRoles(Map<String, List<String>> actionRoles) {
		this.actionRoles = actionRoles;
	}
	
	private List<String> getRolesFor(Menu menu) {
		String action = menu.getAction();
		List<String> roles = !isEmpty(action) ? actionRoles.get(action) : null;
		return roles != null ? roles : Collections.emptyList();
	}
	
	public boolean isPermitted(User user, Menu menu) {
		return !Collections.disjoint(user.getRoleIDs(), getRolesFor(menu));
	}
}