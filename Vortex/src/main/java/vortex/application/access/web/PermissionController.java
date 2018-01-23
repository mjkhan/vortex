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
		return search(hreq, "permission/permission-action");
	}
	
	@RequestMapping("/select.do")
	public ModelAndView select(HttpServletRequest hreq) {
		return search(hreq, "permission/select");
	}

	private ModelAndView search(HttpServletRequest hreq, String initView) {
		DataObject req = request(hreq);
		int start = req.number("start").intValue(),
			fetch = properties.getInt("fetch");
		req.set("start", start)
		   .set("fetch", fetch);
		BoundedList<DataObject> permissions = permissionService.search(req);
		ModelAndView mv = new ModelAndView(req.bool("init") || !req.bool("ajax") ? initView : "jsonView");
		if (!permissions.isEmpty()) {
			String permissionID = permissions.get(0).string("pms_id");
			BoundedList<DataObject> actions = permissionService.getActions(req.set("permissionID", permissionID));
			mv.addObject("actions", actions)
			  .addObject("totalActions", actions.getTotalSize())
			  .addObject("actionStart", 0);
		}
		return mv
			.addObject("permissions", permissions)
			.addObject("totalPermissions", permissions.getTotalSize())
			.addObject("permissionStart", permissions.getStart())
			.addObject("fetch", req.get("fetch"));
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getInfo(@RequestParam(required=false) String permissionID) {
		return new ModelAndView("permission/info")
			.addObject("permission", permissionService.getInfo(permissionID));
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
	
	@RequestMapping("/action/list.do")
	public ModelAndView getActions(HttpServletRequest hreq) {
		DataObject req = request(hreq)
			.set("fetch", properties.getInt("fetch"));
		req.set("start", req.number("start").intValue());
		
		BoundedList<DataObject> actions = permissionService.getActions(req);
		return new ModelAndView("jsonView")
			.addObject("actions", actions)
			.addObject("totalActions", actions.getTotalSize())
			.addObject("actionStart", actions.getStart())
			.addObject("fetch", req.get("fetch"));
	}
	
	@RequestMapping("/action/add.do")
	public ModelAndView addActions(@RequestParam String permissionID, @RequestParam String actionID) {
		return new ModelAndView("jsonView")
			.addObject("saved", permissionService.addActions(permissionID, actionID.split(",")) > 0);
	}
	
	@RequestMapping("/action/delete.do")
	public ModelAndView deleteActions(@RequestParam String permissionID, @RequestParam String actionID) {
		return new ModelAndView("jsonView")
			.addObject("saved", permissionService.deleteActions(permissionID, actionID.split(",")) > 0);
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView remove(@RequestParam String permissionID) {
		return new ModelAndView("jsonView")
			.addObject("saved", permissionService.delete(permissionID.split(",")) > 0);
	}
}