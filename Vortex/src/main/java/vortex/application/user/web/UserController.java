package vortex.application.user.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.User;
import vortex.application.user.service.UserService;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/user")
public class UserController extends ApplicationController {
	@Resource(name="userService")
	private UserService userService;
	
	@RequestMapping("/list.do")
	public ModelAndView search(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("start", req.number("start").intValue())
		   .set("fetch", properties.getInt("fetch"));
		return modelAndView(!req.bool("ajax") ? "user/list" : "jsonView", userService.search(req));
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getUser(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		return modelAndView("user/info", userService.getUser(req));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		User user = new User();
		user.setId(req.string("userID"));
		setUser(user, req);
		return modelAndView("jsonView", userService.create(req.set("user", user)));
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		User user = userService.getUser(req).value("user");
		setUser(user, req);
		return modelAndView("jsonView", userService.update(req.set("user", user)));
	}
	
	private void setUser(User user, DataObject req) {
		user.setName(req.string("userName"));
		user.setAlias(req.string("alias"));
		user.setPassword(req.string("password"));
	}
	
	@RequestMapping("/setStatus.do")
	public ModelAndView setStatus(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		return modelAndView("jsonView", userService.setStatus(req));
	}
	
	@RequestMapping("/remove.do")
	public ModelAndView remove(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		return modelAndView("jsonView", userService.remove(req));
	}
}