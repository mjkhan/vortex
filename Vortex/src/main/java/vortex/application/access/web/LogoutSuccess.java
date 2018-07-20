package vortex.application.access.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Controller;

@Controller("logoutSuccess")
public class LogoutSuccess extends SimpleUrlLogoutSuccessHandler implements ApplicationContextAware, HttpSessionListener {
	@Override
	public void onLogoutSuccess(HttpServletRequest hreq, HttpServletResponse hresp, Authentication authentication) throws IOException, ServletException {
		super.onLogoutSuccess(hreq, hresp, authentication);
	}

	@Override
	public void setApplicationContext(ApplicationContext actx) throws BeansException {
/*		Servlet 3.0 이상
		if (!(actx instanceof WebApplicationContext)) return;
		
		WebApplicationContext wactx = (WebApplicationContext)actx;
		wactx.getServletContext().addListener(this);
*/		
	}

	@Override
	public void sessionCreated(HttpSessionEvent evt) {}

	@Override
	public void sessionDestroyed(HttpSessionEvent evt) {
/*		
		HttpSession session = evt.getSession();
		String sessionID = (String)session.getAttribute(Client.SESSION);
		if (sessionID != null)
			authenticationService.loggedOut(null, sessionID);
*/		
	}
}