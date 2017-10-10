package vortex.application.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import vortex.application.ApplicationAccess;
import vortex.support.data.DataObject;

public interface UserService extends ApplicationAccess, UserDetailsService {
/*
	public DataObject login(DataObject req);
	
	public DataObject logout(DataObject req);
*/	
	public DataObject search(DataObject req);
	
	public DataObject getUser(DataObject req);
	
	public DataObject create(DataObject req);
	
	public DataObject update(DataObject req);
	
	public DataObject setStatus(DataObject req);
	
	public DataObject remove(DataObject req);
	
	public DataObject delete(DataObject req);
}