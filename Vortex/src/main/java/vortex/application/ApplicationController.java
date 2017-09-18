package vortex.application;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.fdl.property.EgovPropertyService;
import vortex.support.AbstractObject;
import vortex.support.data.DataObject;
import vortex.support.web.Kookie;

public class ApplicationController extends AbstractObject {
	@Autowired
	protected EgovPropertyService properties;

	private static final String AJAX = "XMLHttpRequest";
	
	@SuppressWarnings("unchecked")
	protected DataObject request(HttpServletRequest hreq) {
		DataObject req = new DataObject();
		Enumeration<String> names = hreq.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			req.put(name, hreq.getParameter(name));
		}
		String reqWith = hreq.getHeader("X-Requested-With");
		req.set("ajax", AJAX.equalsIgnoreCase(reqWith));
		return req;
	}
	
	protected ModelAndView modelAndView(String viewName, Map<String, ?> map) {
		ModelAndView result = new ModelAndView();
		log().debug(() -> "view name: " + viewName);
		result.setViewName(viewName);
		return result.addAllObjects(map);
	}
	
	public static class Filter implements javax.servlet.Filter {

		@Override
		public void init(FilterConfig cfg) throws ServletException {}

		@Override
		public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {
			HttpServletRequest hreq = (HttpServletRequest)sreq;
			String action = hreq.getRequestURI().replace(hreq.getContextPath(), "");
			System.out.println("action:" + action);
			HttpSession session = hreq.getSession(false);
			if (session != null) {
				boolean newsession = session.isNew();
				String sessionID = session.getId();
				System.out.println("new session:" + newsession);
				System.out.println("session id:" + sessionID);
				System.out.println("JSESSIONID:" + Kookie.get(hreq).getValue("JSESSIONID"));
			} else {
				System.out.println("No session");
			}
			
			chain.doFilter(sreq, sresp);
		}
		
		@Override
		public void destroy() {}
	}
	
	public static class ClientToken {
		private static final String NAME = "vortexToken";
		
		public User.Client read(HttpServletRequest hreq) {
			String token = Kookie.get(hreq).getValue(NAME);
			if (isEmpty(token)) return User.Client.UNKNOWN;
			
			try {
				String[] segs = token.split(";");
				User.Client client = new User.Client();
				client.setId(segs[0]);
				client.setPassword(segs[1]);
				return client;
			} catch (Exception e) {
				return User.Client.UNKNOWN;
			}
		}
		
		public void write(User.Client client, HttpServletRequest hreq, HttpServletResponse hresp) {
			String token = client.getId() + ";" + client.getPassword(); 
			Kookie kookie = Kookie.get(hreq).setResponse(hresp);
			if (!client.isPersistent()) {
				kookie.shortSave(NAME, token);
			} else {
				kookie.longSave(NAME, token);
			}
		}
		
		public void delete(HttpServletRequest hreq, HttpServletResponse hresp) {
			Kookie.get(hreq).setResponse(hresp).remove(NAME);
		}
	}
}