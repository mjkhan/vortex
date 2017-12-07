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

	@SuppressWarnings("unchecked")
	protected DataObject request(HttpServletRequest hreq) {
		DataObject req = new DataObject();
		Enumeration<String> names = hreq.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			req.put(name, hreq.getParameter(name));
		}
		return req;
	}
	
	protected ModelAndView modelAndView(String viewName) {
		ModelAndView result = new ModelAndView();
		log().debug(() -> "view name: " + viewName);
		result.setViewName(viewName);
		return result;
	}
	
	protected ModelAndView modelAndView(String viewName, Map<String, ?> map) {
		return modelAndView(viewName).addAllObjects(map);
	}

	public static class Filter extends AbstractObject implements javax.servlet.Filter {

		@Override
		public void init(FilterConfig cfg) throws ServletException {}

		@Override
		public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {
			HttpServletRequest hreq = (HttpServletRequest)sreq;
			String action = hreq.getRequestURI().replace(hreq.getContextPath(), "");
			Client client = new Client()
				.setAction(action)
				.setIpAddress(sreq.getRemoteAddr())
				.setCurrent();
			log().debug(() -> client + " set as current.");
			HttpSession session = hreq.getSession(false);
			if (session != null) {
				System.out.println("new session:" + session.isNew());
				System.out.println("session id:" + session.getId());
				System.out.println("JSESSIONID:" + Kookie.get(hreq).getValue("JSESSIONID"));
			} else {
				System.out.println("No session");
			}
			
			chain.doFilter(sreq, sresp);
		}
		
		@Override
		public void destroy() {
			Client client = Client.release();
			if (client != null)
				log().debug(() -> client + " released.");
		}
	}
	
}