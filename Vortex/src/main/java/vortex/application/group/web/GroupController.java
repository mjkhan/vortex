package vortex.application.group.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
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
	public ModelAndView createGroup(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Group group = new Group();
		group.setName(req.string("groupName"));
		group.setDescription(req.string("description"));
		Group saved = groupService.create(group);
		return new ModelAndView("jsonView")
			.addObject("saved", saved != null)
			.addObject("groupID", saved.getId());
	}
	
	@RequestMapping("/update.do")
	public ModelAndView updateGroup(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Group group = groupService.getGroup(req.string("groupID"));
		group.setName(req.string("groupName"));
		group.setDescription(req.string("description"));
		return new ModelAndView("jsonView")
			.addObject("saved", groupService.update(group));
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView deleteGroup(@RequestParam String groupID) {
		return new ModelAndView("jsonView")
			.addObject("saved", groupService.deleteGroups(groupID.split(",")));
	}
}