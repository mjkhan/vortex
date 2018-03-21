package vortex.application.access.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Controller;

import vortex.support.Log;

@Controller
public class AuthenticationFailure extends SimpleUrlAuthenticationFailureHandler {
	private static final Log log = Log.get(AuthenticationFailure.class);

	@Override
	public void onAuthenticationFailure(HttpServletRequest hreq, HttpServletResponse hresp, AuthenticationException exception) throws IOException, ServletException {
		log.debug(() -> "Authentication failure");
		String accept = getAccept(hreq);
		if (accept.contains("json")) {
			setContentType(hresp, "application/json");
			print(hresp, "{\"authenticated\":false}");
		} else {
			super.onAuthenticationFailure(hreq, hresp, exception);
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