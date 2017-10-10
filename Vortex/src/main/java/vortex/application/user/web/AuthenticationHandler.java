package vortex.application.user.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import vortex.support.AbstractObject;

public class AuthenticationHandler extends AbstractObject implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest hreq, HttpServletResponse hresp, Authentication authentication) throws IOException, ServletException {
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest hreq, HttpServletResponse hresp, AuthenticationException exception) throws IOException, ServletException {
	}

}