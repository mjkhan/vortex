package vortex.application.access.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import vortex.support.Log;

@Controller("authenticationSuccess")
public class AuthenticationSuccess extends SavedRequestAwareAuthenticationSuccessHandler {
	private static final Log log = Log.get(AuthenticationSuccess.class);
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest hreq, HttpServletResponse hresp, Authentication authentication) throws IOException, ServletException {
		log.debug(() -> "Authentication success");
		String accept = getAccept(hreq);
		if (accept.contains("json")) {
			setContentType(hresp, "application/json");
			print(hresp, "{\"authenticated\":true}");
		} else {
			super.onAuthenticationSuccess(hreq, hresp, authentication);
		}
	}
	
	private String getAccept(HttpServletRequest hreq) {
		String accept = hreq.getHeader("accept");
		return accept != null ? accept : "";
	}
	
	private void setContentType(HttpServletResponse hresp, String contentType) {
		hresp.setContentType(contentType);
		hresp.setCharacterEncoding("utf-8");
	}
	
	private void print(HttpServletResponse hresp, String data) throws IOException {
		try (PrintWriter out = hresp.getWriter()) {
			out.print(data);
			out.flush();
		} catch (IOException e) {
			throw e;
		}
	}
}