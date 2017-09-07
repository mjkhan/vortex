package vortex.application.access.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.access.service.Role;
import vortex.application.access.service.RoleService;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/role")
public class RoleController extends ApplicationController {
	@Resource(name="roleService")
	private RoleService roleService;
	
	@RequestMapping("/list.do")
	public ModelAndView getRoles(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		return modelAndView(!req.bool("ajax") ? "role/list" : "jsonView", roleService.getRoles(req));
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getRole(HttpServletRequest hreq) {
		return modelAndView("role/info", roleService.getRole(request(hreq)));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Role role = new Role();
		role.setId(req.string("roleID"));
		role.setName(req.string("roleName"));
		role.setDescription(req.string("description"));
		return modelAndView("jsonView", roleService.create(req.set("role", role)));
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Role role = roleService.getRole(req).value("role");
		role.setName(req.string("roleName"));
		role.setDescription(req.string("description"));
		return modelAndView("jsonView", roleService.update(req.set("role", role)));
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView delete(HttpServletRequest hreq) {
		return modelAndView("jsonView", roleService.delete(request(hreq)));
	}
}