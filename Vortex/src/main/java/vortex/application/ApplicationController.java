package vortex.application;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import egovframework.rte.fdl.property.EgovPropertyService;
import vortex.application.menu.Menu;
import vortex.application.menu.MenuContext;
import vortex.application.menu.service.MenuService;
import vortex.support.AbstractObject;
import vortex.support.data.DataObject;
import vortex.support.data.hierarchy.Hierarchy;

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
		return req.set("ajax", hreq.getAttribute("ajax"));
	}

	public static class Filter extends AbstractObject implements javax.servlet.Filter {
		private MenuService menuService;
		private String debugDomain;

		@Override
		public void init(FilterConfig cfg) throws ServletException {
			ApplicationContext actx = WebApplicationContextUtils.getRequiredWebApplicationContext(cfg.getServletContext());
			menuService = (MenuService)actx.getBean("menuService");
			debugDomain = ifEmpty(((EgovPropertyService)actx.getBean("propertiesService")).getString("debugDomain"), "");
		}

		@Override
		public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {
			HttpServletRequest hreq = (HttpServletRequest)sreq;
			hreq.setAttribute("ajax", "XMLHttpRequest".equals(hreq.getHeader("X-Requested-With")));
			hreq.setAttribute("debug", debugDomain.contains(hreq.getServerName()));
			
			HttpSession session = hreq.getSession(false);
			hreq.setAttribute("client", 
				new Client()
					.setAction(hreq.getRequestURI().replace(hreq.getContextPath(), ""))
					.setIpAddress(sreq.getRemoteAddr())
					.setNewSession(session != null && session.isNew())
					.setCurrent()
			);
			
			MenuContext mctx = menuService.getMenuContext();
			if (mctx != null) {
				hreq.setAttribute("menuContext", mctx);
				Hierarchy<Menu> menus = mctx.getMenus();
				hreq.setAttribute("menus", menus);
				hreq.setAttribute("topMenus", menus.topElements());
			}

			chain.doFilter(sreq, sresp);
		}
		
		@Override
		public void destroy() {}
	}
}