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
		log().debug(() -> "Searching Users...");
		DataObject params = ifEmpty(req, this::params);
		
		if (isEmpty(req.get("field")))
			req.remove("field");
		if (isEmpty(req.get("value")))
			req.remove("value");
		
		List<DataObject> list = selectList("user.search", params);
		return boundedList(list, params);
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
	
	public int create(User user) {
		User currentUser = currentUser();
		if (currentUser.isUnknown())
			currentUser = user;
		return user != null ? insert("user.insert",
			params(false)
				.set("user", user)
				.set("currentUser", currentUser)
		) : 0;
	}
	
	public int update(User user) {
		return user != null ? update("user.update", params().set("user", user)) : 0;
	}
	
	public int setStatus(String status, String... userIDs) {
		return isEmpty(status) ? 0 :
		update(
			"user.setStatus",
			params().set("status", status)
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