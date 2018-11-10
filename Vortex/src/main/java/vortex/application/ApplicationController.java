package vortex.application;

import java.io.IOException;
import java.util.Enumeration;

import javax.annotation.Resource;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.rte.fdl.property.EgovPropertyService;
import vortex.application.menu.MenuContext;
import vortex.application.menu.service.MenuService;
import vortex.support.AbstractComponent;
import vortex.support.data.DataObject;
import vortex.support.web.ClientAddress;

public class ApplicationController extends AbstractComponent {
	@Autowired
	protected EgovPropertyService properties;
	@Resource(name="objectMapper")
	protected ObjectMapper objectMapper;

	@SuppressWarnings("unchecked")
	protected DataObject request(HttpServletRequest hreq) {
		DataObject req = new DataObject();
		Enumeration<String> names = hreq.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			req.put(name, hreq.getParameter(name));
		}
		return req.set("ajax", hreq.getAttribute("ajax"))
				  .set("json", hreq.getAttribute("json"));
	}
	
	protected String inJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw runtimeException(e);
		}
	}

	public static class Filter extends AbstractComponent implements javax.servlet.Filter {
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
			hreq.setAttribute("json", ifEmpty(hreq.getHeader("accept"), "").contains("json"));
			hreq.setAttribute("debug", debugDomain.contains(hreq.getServerName()));
			String userAgent = hreq.getHeader("User-Agent");
			
			HttpSession session = hreq.getSession(false);
			hreq.setAttribute("client", 
				new Access()
					.setAction(hreq.getRequestURI().replace(hreq.getContextPath(), ""))
					.setIpAddress(ClientAddress.get(hreq))
					.setNewSession(session != null && session.isNew())
					.setMobile(userAgent.contains("Mobi"))
					.setCurrent()
					.setSessionID(session != null ? session.getId() : null)
			);

			MenuContext mctx = menuService.getMenuContext();
			if (mctx != null) {
				hreq.setAttribute("menuContext", mctx);
				hreq.setAttribute("menus", mctx);
				hreq.setAttribute("topMenus", mctx.topElements());
			}

			chain.doFilter(sreq, sresp);
		}
		
		@Override
		public void destroy() {}
	}
}