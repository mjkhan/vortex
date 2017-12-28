package vortex.application.user.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import vortex.application.DataMapper;
import vortex.application.User;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

@Repository("userMapper")
public class UserMapper extends DataMapper {
	
	public BoundedList<DataObject> search(DataObject req) {
		DataObject params = ifEmpty(req, this::params);
		
		if (isEmpty(req.get("field")))
			req.remove("field");
		if (isEmpty(req.get("value")))
			req.remove("value");
		
		return boundedList(selectList("user.search", params), params);
	}
	
	public List<DataObject> getInfo(String... userIDs) {
		return selectList("user.getInfo", params().set("userIDs", userIDs));
	}
	
	public List<User> getUsers(String... userIDs) {
		return selectList("user.getUsers", params().set("userIDs", userIDs));
	}
	
	public User getUser(String userID) {
		List<User> users = getUsers(userID);
		return !users.isEmpty() ? users.get(0) : null;
	}
	
	public boolean create(User user) {
		if (user == null) return false;
		
		User currentUser = currentUser();
		if (currentUser.isUnknown())
			currentUser = user;
		return insert("user.insert",
			params()
				.set("user", user)
				.set("currentUser", currentUser)
		) == 1;
	}
	
	public boolean update(User user) {
		if (user == null) return false;
		
		return update("user.update", params(true).set("user", user)) == 1;
	}
	
	public int setStatus(String status, String... userIDs) {
		return isEmpty(status) ? 0 :
		update(
			"user.setStatus",
			params(true)
				.set("status", status)
				.set("userIDs", userIDs)
		);
	}
	
	public int remove(String... userIDs) {
		return setStatus(Status.REMOVED.code(), userIDs);
	}
	
	public int deleteUsers(String... userIDs) {
		return delete(
			"user.delete",
			params().set("userIDs", userIDs)
		);
	}
}