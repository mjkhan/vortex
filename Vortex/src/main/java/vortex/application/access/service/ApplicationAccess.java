package vortex.application.access.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import vortex.application.Access;
import vortex.application.User;
import vortex.support.AbstractComponent;

public class ApplicationAccess extends AbstractComponent implements AccessDecisionVoter<Object> {
	@Autowired
	private ActionService actionService;
	
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object obj, Collection<ConfigAttribute> attributes) {
		User user = User.get(authentication);
		Access access = Access.current();
		switch (actionService.getPermission(user.getId(), access.getAction())) {
		case GRANTED: return ACCESS_GRANTED;
		case DENIED: return ACCESS_DENIED;
		default: return ACCESS_ABSTAIN;
		}
	}
}