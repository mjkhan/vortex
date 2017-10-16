package vortex.application.access.service;

import org.springframework.security.core.GrantedAuthority;

import vortex.application.group.AbstractGroup;

public class Role extends AbstractGroup implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	@Override
	public String getAuthority() {
		return getId();
	}
}