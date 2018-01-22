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
		ModelAndView mv = new ModelAndView(!req.bool("ajax") ? "role/roles" : "jsonView")
			.addObject("roles", roles)
			.addObject("totalRoles", roles.getTotalSize())
			.addObject("roleStart", roles.getStart());
		if (!roles.isEmpty()) {
			req.set("groupID", roles.get(0).string("GRP_ID"))
			   .set("start", 0);
			switch (ifEmpty(req.string("memberType"), RoleService.USER)) {
			case RoleService.USER: getUsers(req, mv); break;
			case RoleService.PERMISSION: getPermissions(req, mv); break;
			}
		}
		
		return mv.addObject("fetch", properties.getInt("fetch"));
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
	
	private ModelAndView getUsers(DataObject req, ModelAndView mv) {
		req.set("start", req.number("start").intValue())
		   .set("fetch", properties.getInt("fetch"));
		BoundedList<DataObject> users = roleService.getUsers(req);
		return mv.addObject("users", users)
		  .addObject("totalUsers", users.getTotalSize())
		  .addObject("userStart", users.getStart())
		  .addObject("fetch", users.getFetchSize());
	}

	@RequestMapping("/user/list.do")
	public ModelAndView getUsers(HttpServletRequest hreq) {
		return getUsers(request(hreq), new ModelAndView("jsonView"));
	}

	@RequestMapping("/user/add.do")
	public ModelAndView addUsers(@RequestParam String roleID, @RequestParam String userID) {
		int affected = roleService.addUsers(roleID.split(","), userID.split(","));
		return new ModelAndView("jsonView")
			.addObject("affected", affected)
			.addObject("saved", affected > 0);
	}

	@RequestMapping("/user/delete.do")
	public ModelAndView deleteUsers(@RequestParam String roleID, @RequestParam String userID) {
		int affected = roleService.deleteUsers(roleID.split(","), userID.split(","));
		return new ModelAndView("jsonView")
			.addObject("affected", affected)
			.addObject("saved", affected > 0);
	}
	
	private ModelAndView getPermissions(DataObject req, ModelAndView mv) {
		req.set("start", req.number("start").intValue())
		   .set("fetch", properties.getInt("fetch"));
		BoundedList<DataObject> permissions = roleService.getPermissions(req);
		return mv
			.addObject("permissions", permissions)
			.addObject("totalPermissions", permissions.getTotalSize())
			.addObject("permissionStart", permissions.getStart())
			.addObject("fetch", permissions.getFetchSize());
	}

	@RequestMapping("/permission/list.do")
	public ModelAndView getPermissions(HttpServletRequest hreq) {
		return getPermissions(request(hreq), new ModelAndView("jsonView"));
	}

	@RequestMapping("/permission/add.do")
	public ModelAndView addPermissions(@RequestParam String roleID, @RequestParam String permissionID) {
		int affected = roleService.addPermissions(roleID.split(","), permissionID.split(","));
		return new ModelAndView("jsonView")
			.addObject("affected", affected)
			.addObject("saved", affected > 0);
	}

	@RequestMapping("/permission/delete.do")
	public ModelAndView deletePermissions(@RequestParam String roleID, @RequestParam String permissionID) {
		int affected = roleService.deletePermissions(roleID.split(","), permissionID.split(","));
		return new ModelAndView("jsonView")
			.addObject("affected", affected)
			.addObject("saved", affected > 0);
	}
}