package vortex.application.access.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import vortex.application.group.AbstractGroup;

public class Role extends AbstractGroup implements GrantedAuthority {
	private static final long serialVersionUID = 1L;
	
	public static boolean contains(List<String> userRoles, List<String> actionRoles) {
		return !Collections.disjoint(userRoles, actionRoles);
	}

	@Override
	public String getAuthority() {
		return getId();
	}
}