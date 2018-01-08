package vortex.application.access.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.access.service.Permission;
import vortex.application.access.service.PermissionService;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/permission")
public class PermissionController extends ApplicationController {
	@Resource(name="permissionService")
	private PermissionService permissionService;
	
	@RequestMapping("/list.do")
	public ModelAndView search(HttpServletRequest hreq) {
		return search(hreq, "permission/list");
	}
	
	@RequestMapping("/select.do")
	public ModelAndView select(HttpServletRequest hreq) {
		return search(hreq, "permission/select");
	}

	private ModelAndView search(HttpServletRequest hreq, String initView) {
		DataObject req = request(hreq);
		req.set("start", req.number("start").intValue())
		   .set("fetch", properties.getInt("fetch"));
		BoundedList<DataObject> permissions = permissionService.search(req);
		return new ModelAndView(req.bool("init") || !req.bool("ajax") ? initView : "jsonView")
			.addObject("permissions", permissions)
			.addObject("totalSize", permissions.getTotalSize())
			.addObject("start", req.get("start"))
			.addObject("fetchSize", req.get("fetch"));
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getInfo(@RequestParam(required=false) String pmsID) {
		return new ModelAndView("permission/info")
			.addObject("permission", permissionService.getInfo(pmsID));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(@ModelAttribute Permission permission) {
		return new ModelAndView("jsonView")
			.addObject("saved", permissionService.create(permission));
	}

	@RequestMapping("/update.do")
	public ModelAndView update(@ModelAttribute Permission permission) {
		return new ModelAndView("jsonView")
			.addObject("saved", permissionService.update(permission));
	}
	
	@RequestMapping("/action/add.do")
	public ModelAndView addActions(@RequestParam String pmsID, @RequestParam String actionID) {
		return new ModelAndView("jsonView")
			.addObject("saved", permissionService.addActions(pmsID, actionID.split(",")) > 0);
	}
	
	@RequestMapping("/action/delete.do")
	public ModelAndView deleteActions(@RequestParam String pmsID, @RequestParam String actionID) {
		return new ModelAndView("jsonView")
			.addObject("saved", permissionService.deleteActions(pmsID, actionID.split(",")) > 0);
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView remove(@RequestParam String pmsID) {
		return new ModelAndView("jsonView")
			.addObject("saved", permissionService.delete(pmsID.split(",")) > 0);
	}
}