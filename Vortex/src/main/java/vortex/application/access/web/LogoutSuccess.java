package vortex.application.access.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Controller;

@Controller("logoutSuccess")
public class LogoutSuccess extends SimpleUrlLogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest hreq, HttpServletResponse hresp, Authentication authentication) throws IOException, ServletException {
		super.onLogoutSuccess(hreq, hresp, authentication);
	}
}