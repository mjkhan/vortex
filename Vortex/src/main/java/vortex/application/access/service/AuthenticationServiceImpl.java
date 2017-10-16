package vortex.application.access.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.application.User;
import vortex.application.user.service.UserMapper;

@Service("authenticationService")
public class AuthenticationServiceImpl extends ApplicationService implements AuthenticationService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMemberMapper roleMemberMapper;

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.getUser(username);
		if (user == null)
			throw new UsernameNotFoundException("User not found with the username: " + username);
		
		List<Role> roles = roleMemberMapper.getRoles(username);
		user.setAuthorities(roles);
		
		return user;
	}
}