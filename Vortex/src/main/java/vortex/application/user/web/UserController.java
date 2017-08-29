package vortex.application.user.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.user.service.UserService;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/user")
public class UserController extends ApplicationController {
	@Resource(name="userService")
	private UserService userService;
	
	@RequestMapping("/list.do")
	public ModelAndView search(HttpServletRequest hreq) {
		DataObject req = request(hreq),
				   result = userService.search(req);
		return modelAndView(!req.bool("ajax") ? "user/list" : "jsonView", result);
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getInfo(HttpServletRequest hreq) {
		DataObject req = request(hreq),
				   result = userService.getUser(req);
		return modelAndView(!req.bool("ajax") ? "user/info" : "jsonView", result);
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(HttpServletRequest hreq) {
		DataObject req = request(hreq),
				   result = userService.create(req);
		return modelAndView(!req.bool("ajax") ? "user/info" : "jsonView", result);
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(HttpServletRequest hreq) {
		DataObject req = request(hreq),
				   result = userService.update(req);
		return modelAndView(!req.bool("ajax") ? "user/info" : "jsonView", result);
	}
}