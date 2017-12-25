package vortex.application.user.service;

import vortex.application.User;
import vortex.support.data.DataObject;

public interface UserService {
	public DataObject search(DataObject req);
	
	public DataObject getInfo(String userID);
	
	public User getUser(String userID);
	
	public boolean create(User user);
	
	public boolean update(User user);
	
	public boolean setStatus(String status, String... userIDs);
	
	public boolean remove(String... userIDs);
	
	public boolean delete(String... userIDs);
}