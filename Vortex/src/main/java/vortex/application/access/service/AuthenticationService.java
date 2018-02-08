package vortex.application.access.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {
	public void update(String... userIDs);
	
	public void expire(String... userIDs);
}