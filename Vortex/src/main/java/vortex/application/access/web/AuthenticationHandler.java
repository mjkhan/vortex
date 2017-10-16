package vortex.application.access.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vortex.application.ApplicationController;

@Controller
public class AuthenticationHandler extends ApplicationController implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

	@RequestMapping(value="/login.do", method=RequestMethod.GET)
	public String loginPage() {
		return "user/login";
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest hreq, HttpServletResponse hresp, Authentication authentication) throws IOException, ServletException {
		log().debug(() -> "Authentication success");
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest hreq, HttpServletResponse hresp, AuthenticationException exception) throws IOException, ServletException {
		log().debug(() -> "Authentication failure");
	}

}