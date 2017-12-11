package vortex.application.menu;

import java.util.ArrayList;
import java.util.Collection;
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
	
	public List<Menu> getMenus(int level) {
		Collection<Menu> tops = menus.topElements();
		ArrayList<Menu> result = new ArrayList<>();
		if (level == 0) {
			result.addAll(tops);
			return result;
		}
		
		return null;
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
	
	private List<String> getRolesFor(Menu menu) {
		String action = menu.getAction();
		List<String> roles = !isEmpty(action) ? actionRoles.get(action) : null;
		return roles != null ? roles : Collections.emptyList();
	}
	
	public boolean isPermitted(User user, Menu menu) {
		return !Collections.disjoint(user.getRoleIDs(), getRolesFor(menu));
	}
	
	public boolean isPermitted(Menu menu) {
		User user = User.current();
		if (user == null) return false;
		return isPermitted(user, menu);
	}
}