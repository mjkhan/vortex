package vortex.application.access.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.access.service.RoleService;
import vortex.application.group.Group;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/role")
public class RoleController extends ApplicationController {
	@Resource(name="roleService")
	private RoleService roleService;

	@RequestMapping("/list.do")
	public ModelAndView search(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("start", req.number("start").intValue())
		   .set("fetch", properties.getInt("fetch"));
		BoundedList<DataObject> roles = roleService.search(req);
		return new ModelAndView(!req.bool("ajax") ? "role/roles" : "jsonView")
			.addObject("roles", roles)
			.addObject("totalRoles", roles.getTotalSize())
			.addObject("roleStart", roles.getStart())
			.addObject("fetch", req.get("fetch"));
	}

	@RequestMapping("/info.do")
	public ModelAndView getInfo(@RequestParam(required=false) String roleID) {
		return new ModelAndView("role/info")
			.addObject("role", roleService.getInfo(roleID));
	}

	@RequestMapping("/create.do")
	public ModelAndView create(@ModelAttribute Group role) {
		return new ModelAndView("jsonView")
			.addObject("saved", roleService.create(role))
			.addObject("roleID", role.getId());
	}

	@RequestMapping("/update.do")
	public ModelAndView update(@ModelAttribute Group role) {
		return new ModelAndView("jsonView")
			.addObject("saved", roleService.update(role));
	}

	@RequestMapping("/delete.do")
	public ModelAndView delete(@RequestParam String roleID) {
		return new ModelAndView("jsonView")
			.addObject("saved", roleService.delete(roleID.split(",")) > 0);
	}
/*
	@RequestMapping("/action/list.do")
	public ModelAndView getActions(HttpServletRequest hreq) {
		DataObject req = request(hreq),
				   res = new DataObject();
		String roleID = req.string("roleID");
		boolean init = isEmpty(roleID);
		if (init) {
			List<DataObject> roles = roleService.getRolesFor(req.set("member", "action")).value("roles");
			roleID = !isEmpty(roles) ? roles.get(0).string("ROLE_ID") : null;
			res.set("roles", roles);
		}
		if (!isEmpty(roleID)) {
			res.set("actions", roleService.getActions(req.set("roleID", roleID)).value("actions"));
		}
		return modelAndView(init ? "role/actions" : "jsonView", res);
	}
*/
	@RequestMapping("/action/add.do")
	public ModelAndView addActions(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("roleIDs", req.string("roleIDs").split(","))
		   .set("actionIDs", req.string("actionIDs").split(","));
		return modelAndView("jsonView", roleService.addActions(req));
	}

	@RequestMapping("/action/delete.do")
	public ModelAndView deleteActions(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("roleIDs", req.string("roleIDs").split(","))
		   .set("actionIDs", req.string("actionIDs").split(","));
		return modelAndView("jsonView", roleService.deleteActions(req));
	}
/*
	@RequestMapping("/user/list.do")
	public ModelAndView getUsers(HttpServletRequest hreq) {
		DataObject req = request(hreq),
				   res = new DataObject();
		String roleID = req.string("roleID");
		boolean init = isEmpty(roleID);
		if (init) {
			List<DataObject> roles = roleService.getRoles(req).value("roles");
			roleID = !isEmpty(roles) ? roles.get(0).string("ROLE_ID") : null;
			res.set("roles", roles);
		}
		if (!isEmpty(roleID)) {
			res.set("users", roleService.getUsers(req.set("roleID", roleID)).value("users"));
		}
		return modelAndView(init ? "role/users" : "jsonView", res);
	}
*/
	@RequestMapping("/user/add.do")
	public ModelAndView addUsers(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("roleIDs", req.string("roleIDs").split(","))
		   .set("userIDs", req.string("userIDs").split(","));
		return modelAndView("jsonView", roleService.addUsers(req));
	}

	@RequestMapping("/user/delete.do")
	public ModelAndView deleteUsers(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("roleIDs", req.string("roleIDs").split(","))
		   .set("userIDs", req.string("userIDs").split(","));
		return modelAndView("jsonView", roleService.deleteUsers(req));
	}
}