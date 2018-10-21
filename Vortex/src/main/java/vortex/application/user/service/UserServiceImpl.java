package vortex.application.user.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.User;
import vortex.application.group.GroupMapper;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Service("userService")
public class UserServiceImpl extends ApplicationService implements UserService {
	@Resource(name="userMapper")
	private UserMapper userMapper;
	@Resource(name="roleGroup")
	private GroupMapper roleGroup;

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
	
	private int deleteUserRoles(String... userIDs) {
		return roleGroup.deleteMembers((String[])null, "000" /* RoleService.USER */, userIDs);
	}

	@Override
	public int remove(String... userIDs) {
		return deleteUserRoles(userIDs)
			 + userMapper.remove(userIDs);
	}

	@Override
	public int delete(String... userIDs) {
		return deleteUserRoles(userIDs)
			 + userMapper.deleteUsers(userIDs);
	}

	@Override
	public boolean updatePassword(String userID, String old, String password) {
		return userMapper.updatePassword(userID, old, password);
	}

	@Override
	public int initFailedLogin(String... userIDs) {
		return userMapper.initFailedLogin(userIDs);
	}

	@Override
	public int activate(String... userIDs) {
		return userMapper.activate(userIDs);
	}
}