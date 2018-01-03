package vortex.application.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.User;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Service("userService")
public class UserServiceImpl extends ApplicationService implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public BoundedList<DataObject> search(DataObject req) {
		return userMapper.search(req);
	}

	@Override
	public DataObject getInfo(String userID) {
		List<DataObject> info = userMapper.getInfo(userID);
		return !isEmpty(info) ? info.get(0) : null;
	}
	
	@Override
	public User getUser(String userID) {
		return userMapper.getUser(userID);
	}

	@Override
	public boolean create(User user) {
		return userMapper.create(user);
	}

	@Override
	public boolean update(User user) {
		return userMapper.update(user);
	}

	@Override
	public boolean setStatus(String status, String... userIDs) {
		return userMapper.setStatus(status, userIDs) > 0;
	}

	@Override
	public boolean remove(String... userIDs) {
		return userMapper.remove(userIDs) > 0;
	}

	@Override
	public boolean delete(String... userIDs) {
		return userMapper.deleteUsers(userIDs) > 0;
	}
}