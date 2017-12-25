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
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

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
		private MenuService menuService;

		@Override
		public void init(FilterConfig cfg) throws ServletException {
			ApplicationContext actx = WebApplicationContextUtils.getRequiredWebApplicationContext(cfg.getServletContext());
			menuService = (MenuService)actx.getBean("menuService");
		}

		@Override
		public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {
			HttpServletRequest hreq = (HttpServletRequest)sreq;
			hreq.setAttribute("ajax", "XMLHttpRequest".equals(hreq.getHeader("X-Requested-With")));

			HttpSession session = hreq.getSession(false);
			hreq.setAttribute("client", 
				new Client()
					.setAction(hreq.getRequestURI().replace(hreq.getContextPath(), ""))
					.setIpAddress(sreq.getRemoteAddr())
					.setNewSession(session != null && session.isNew())
					.setCurrent()
			);
			hreq.setAttribute("currentUser", User.current());
			
			MenuContext mctx = menuService.getMenuCotext();
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