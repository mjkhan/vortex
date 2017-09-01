package vortex.application;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.fdl.property.EgovPropertyService;
import vortex.support.AbstractObject;
import vortex.support.data.DataObject;

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
}