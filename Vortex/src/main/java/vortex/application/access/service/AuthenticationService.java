package vortex.application.access.service;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {
	public void update(String... userIDs);
	
	public Date onSuccess(Object obj);
	
	public void onFailure(Object obj);
	
	public void expire(String... userIDs);
}