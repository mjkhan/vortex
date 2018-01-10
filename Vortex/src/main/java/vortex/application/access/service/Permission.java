package vortex.application.access.service;

import org.springframework.security.core.GrantedAuthority;

import vortex.application.group.AbstractGroup;

public class Permission extends AbstractGroup implements GrantedAuthority {
	private static final long serialVersionUID = 1L;
	
	public static enum Status {
		GRANTED,
		DENIED,
		ACTION_NOT_FOUND
	}

	@Override
	public String getAuthority() {
		return getId();
	}
}