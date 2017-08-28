package vortex.application.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Service("userService")
public class UserServiceImpl extends ApplicationService implements UserService {
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public DataObject search(DataObject req) {
		BoundedList<DataObject> users = userMapper.search(req);
		return dataobject()
			.set("users", users)
			.set("more", users.hasNext())
			.set("next", users.getEnd() + 1);
	}

	@Override
	public DataObject getUser(DataObject req) {
		return dataobject()
			.set("user", userMapper.getUser(req.string("userID")));
	}

	@Override
	public DataObject create(DataObject req) {
		User user = req.value("user");
		int saved = userMapper.create(user);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject update(DataObject req) {
		User user = req.value("user");
		int saved = userMapper.update(user);
		return dataobject()
			.set("saved", saved == 1);
	}

	@Override
	public DataObject setStatus(DataObject req) {
		String status = req.string("status");
		String[] userIDs = req.string("userID").split(",");
		int saved = userMapper.setStatus(status, userIDs);
		return dataobject()
			.set("saved", saved > 0);
	}

	@Override
	public DataObject remove(DataObject req) {
		String[] userIDs = req.string("userID").split(",");
		int saved = userMapper.remove(userIDs);
		return dataobject()
			.set("saved", saved > 0);
	}

	@Override
	public DataObject delete(DataObject req) {
		String s = req.string("userID");
		String[] userIDs = !isEmpty(s) ? s.split(",") : null;
		int saved = userMapper.deleteUsers(userIDs);
		return dataobject()
			.set("saved", saved > 0);
	}

}