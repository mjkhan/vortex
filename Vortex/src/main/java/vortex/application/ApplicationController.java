package vortex.application;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import vortex.support.AbstractObject;
import vortex.support.data.DataObject;

@Controller
public class ApplicationController extends AbstractObject {
	@SuppressWarnings("unchecked")
	protected DataObject request(HttpServletRequest hreq) {
		DataObject req = new DataObject();
		req.putAll(hreq.getParameterMap());
		return req;
	}

	protected ModelAndView setViewName(ModelAndView mv, String viewName) {
		mv.setViewName(viewName);
		if (!isEmpty(viewName))
			log().debug(() -> "view name: " + viewName);
		return mv;
	}
	
	protected ModelAndView modelAndView(String viewName, Map<String, ?> map) {
		ModelAndView result = new ModelAndView();
		if (!isEmpty(map))
			map.forEach((key, value) -> result.addObject(key, value));
		return setViewName(result, viewName);
	}
}