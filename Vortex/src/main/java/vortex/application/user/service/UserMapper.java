package vortex.application.user.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import vortex.application.DataMapper;
import vortex.application.User;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

@Repository("userMapper")
public class UserMapper extends DataMapper {
	public BoundedList<DataObject> search(DataObject params) {
		if (isEmpty(params.get("searchTerms"))) {
			params.remove("searchBy");
			params.remove("searchTerms");
		}
		
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
		return user == null ? false :
			update("user.update", params(true).set("user", user)) == 1;
	}
	
	public boolean updatePassword(String userID, String old, String password) {
		User user = getUser(userID);
		if (!user.getPassword().equals(old))
			throw exception(null).setCode("passwordMismatch");
		return update(
			"user.updatePassword",
			params(true)
				.set("userID", userID)
				.set("password", password)
		) == 1;
	}
	
	public boolean setLogin(String userID, boolean success) {
		return update(
			"user.updateFailedLogin",
			params(true)
				.set("userIDs", Arrays.asList(userID))
				.set("login", success)
		) == 1;
	}
	
	public int initFailedLogin(String... userIDs) {
		return update(
			"user.updateFailedLogin",
			params(true)
				.set("userIDs", userIDs)
				.set("init", true)
		);
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
	
	public int activate(String... userIDs) {
		return setStatus(Status.ACTIVE.code(), userIDs);
	}
	
	public int deleteUsers(String... userIDs) {
		return delete(
			"user.delete",
			params().set("userIDs", userIDs)
		);
	}
}