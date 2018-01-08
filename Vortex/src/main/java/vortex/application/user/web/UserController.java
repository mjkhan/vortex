package vortex.application.user.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.User;
import vortex.application.user.service.UserService;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/user")
public class UserController extends ApplicationController {
	@Resource(name="userService")
	private UserService userService;
	
	@RequestMapping(value="/login.do", method=RequestMethod.GET)
	public String loginPage() {
		return "user/login";
	}
	
	@RequestMapping("/list.do")
	public ModelAndView search(HttpServletRequest hreq) {
		return search(hreq, "user/list");
	}
	
	@RequestMapping("/select.do")
	public ModelAndView select(HttpServletRequest hreq) {
		return search(hreq, "user/select");
	}

	private ModelAndView search(HttpServletRequest hreq, String initView) {
		DataObject req = request(hreq);
		req.set("start", req.number("start").intValue())
		   .set("fetch", properties.getInt("fetch"));
		BoundedList<DataObject> users = userService.search(req);
		return new ModelAndView(req.bool("init") || !req.bool("ajax") ? initView : "jsonView")
			.addObject("users", users)
			.addObject("totalSize", users.getTotalSize())
			.addObject("start", req.get("start"))
			.addObject("fetchSize", req.get("fetch"));
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getInfo(@RequestParam(required=false) String userID) {
		return new ModelAndView("user/info")
			.addObject("user", userService.getInfo(userID));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(@ModelAttribute User user) {
		return new ModelAndView("jsonView")
			.addObject("saved", userService.create(user));
	}

	@RequestMapping("/update.do")
	public ModelAndView update(@ModelAttribute User user) {
		return new ModelAndView("jsonView")
			.addObject("saved", userService.update(user));
	}

	@RequestMapping("/setStatus.do")
	public ModelAndView setStatus(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		String status = notEmpty(req.string("status"), "status"),
			   userID = notEmpty(req.string("userID"), "userID");
		return new ModelAndView("jsonView")
			.addObject("saved",
				userService.setStatus(status, userID.split(","))
			);
	}
	
	@RequestMapping("/remove.do")
	public ModelAndView remove(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		String userID = notEmpty(req.string("userID"), "userID");
		return new ModelAndView("jsonView")
			.addObject("saved",
				userService.remove(userID.split(","))
			);
	}
}