package vortex.application.group.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.group.Group;
import vortex.application.group.service.GroupService;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/group")
public class GroupController extends ApplicationController {
	@Resource(name="groupService")
	private GroupService groupService;
	
	@RequestMapping("/list.do")
	public ModelAndView getGroups(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		int start = Integer.parseInt(ifEmpty(req.string("start"), "0"));
		req.set("start", start)
		   .set("fetch", properties.getInt("fetch"));
		return modelAndView(!req.bool("ajax") ? "group/list" : "jsonView", groupService.getGroups(req)); 
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getGroup(HttpServletRequest hreq) {
		return modelAndView("group/info", groupService.getGroup(request(hreq))); 
	}
	
	@RequestMapping("/create.do")
	public ModelAndView createGroup(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Group group = new Group();
		group.setId(req.string("groupID"));
		group.setName(req.string("groupName"));
		group.setDescription(req.string("description"));
		return modelAndView("jsonView", groupService.createGroup(req.set("group", group)));
	}
	
	@RequestMapping("/update.do")
	public ModelAndView updateGroup(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Group group = groupService.getGroup(req).value("group");
		group.setName(req.string("groupName"));
		group.setDescription(req.string("description"));
		return modelAndView("jsonView", groupService.updateGroup(req.set("group", group)));
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView deleteGroup(HttpServletRequest hreq) {
		return modelAndView("jsonView", groupService.deleteGroups(request(hreq)));
	}
}