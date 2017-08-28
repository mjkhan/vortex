package vortex.application.user.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.user.service.UserService;
import vortex.support.data.DataObject;

@RequestMapping("/user")
public class UserController extends ApplicationController {
	@Resource(name="userService")
	private UserService userService;
	
	public ModelAndView search(HttpServletRequest hreq) {
		DataObject result = userService.search(request(hreq));
		return modelAndView("user/list", result);
	}
}