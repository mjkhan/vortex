package vortex.application.group.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
		req.set("start", req.number("start").intValue())
		   .set("fetch", properties.getInt("fetch"));
		return new ModelAndView(!req.bool("ajax") ? "group/list" : "jsonView")
			.addAllObjects(groupService.getGroups(req)); 
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getGroup(@RequestParam(required=false) String groupID) {
		return new ModelAndView("group/info")
			.addObject("group", groupService.getInfo(groupID)); 
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(@ModelAttribute Group group) {
		boolean saved = groupService.create(group);
		return new ModelAndView("jsonView")
			.addObject("saved", saved)
			.addObject("groupID", saved ? group.getId() : "");
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(@ModelAttribute Group group) {
		return new ModelAndView("jsonView")
			.addObject("saved", groupService.update(group));
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView delete(@RequestParam String groupID) {
		return new ModelAndView("jsonView")
			.addObject("saved", groupService.delete(groupID.split(",")));
	}
}