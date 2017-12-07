package vortex.application.menu;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import vortex.support.data.hierarchy.Hierarchy;

public class MenuContext {
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
	
	public List<String> getPermittedRoles(Menu menu) {
		String action = menu.getAction();
		List<String> roles = actionRoles.get(action);
		return roles != null ? roles : Collections.emptyList();
	}
}