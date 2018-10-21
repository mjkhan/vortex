package vortex.application.user.service;

import vortex.application.User;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

public interface UserService {
	public BoundedList<DataObject> search(DataObject req);
	
	public DataObject getInfo(String userID);
	
	public User getUser(String userID);
	
	public boolean create(User user);
	
	public boolean update(User user);
	
	public boolean updatePassword(String userID, String old, String password);
	
	public int initFailedLogin(String... userIDs);
	
	public int remove(String... userIDs);
	
	public int activate(String... userIDs);
	
	public int delete(String... userIDs);
}