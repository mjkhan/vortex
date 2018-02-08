package vortex.application.access.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.User;
import vortex.application.user.service.UserMapper;

@Service("authenticationService")
public class AuthenticationServiceImpl extends ApplicationService implements AuthenticationService {
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		update();
		User user = userMapper.getUser(notEmpty(username, "username"));
		if (user == null)
			throw new UsernameNotFoundException("User not found named '" + username + "'");
		
		List<Permission> permissions = permissionMapper.getPermissions(username);
		user.setAuthorities(permissions);

		return user;
	}

	@Override
	public void update(String... userIDs) {
		sessionRegistry.getAllPrincipals().forEach(System.out::println);
	}

	@Override
	public void expire(String... userIDs) {
		sessionRegistry.getAllPrincipals();
	}
}