package vortex.application.access.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.access.service.Action;
import vortex.application.access.service.ActionService;
import vortex.application.group.Group;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/action")
public class ActionController extends ApplicationController {
	@Resource(name="actionService")
	private ActionService actionService;
	
	@RequestMapping("/group/list.do")
	public ModelAndView getGroups(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("start", req.number("start").intValue())
		   .set("fetch", properties.getInt("fetch"));
		return new ModelAndView(!req.bool("ajax") ? "action/group/list" : "jsonView")
			.addAllObjects(actionService.getGroups(req)); 
	}
	
	@RequestMapping("/group/info.do")
	public ModelAndView getGroupInfo(@RequestParam(required=false) String groupID) {
		return new ModelAndView("action/group/info")
			.addObject("group", actionService.getGroupInfo(groupID)); 
	}
	
	@RequestMapping("/group/create.do")
	public ModelAndView create(@ModelAttribute Group group) {
		return new ModelAndView("jsonView")
			.addObject("saved", actionService.create(group))
			.addObject("groupID", group.getId());
	}
	
	@RequestMapping("/group/update.do")
	public ModelAndView update(@ModelAttribute Group group) {
		return new ModelAndView("jsonView")
			.addObject("saved", actionService.update(group));
	}
	
	@RequestMapping("/group/delete.do")
	public ModelAndView deleteGroup(@RequestParam String groupID) {
		return new ModelAndView("jsonView")
			.addObject("saved", actionService.deleteGroups(groupID.split(",")) > 0);
	}
	
	@RequestMapping("/select.do")
	public ModelAndView select(HttpServletRequest hreq) {
		return search(hreq, "action/select");
	}
	
	@RequestMapping("/list.do")
	public ModelAndView getActions(HttpServletRequest hreq) {
		return search(hreq, "action/list");
	}

	private ModelAndView search(HttpServletRequest hreq, String initView) {
		DataObject req = request(hreq);
		String groupID = req.string("groupID");
		boolean init = isEmpty(groupID);
		ModelAndView mav = new ModelAndView(init ? initView : "jsonView");
		if (init) {
			List<DataObject> groups = actionService.getGroups(req).value("groups");
			mav.addObject("groups", groups);
			if (!groups.isEmpty()) {
				groupID = groups.get(0).string("grp_id");
				req.set("groupID", groupID);
			}
		}
		return mav.addObject("actions", actionService.getActions(req).value("actions"));
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getInfo(@RequestParam(required=false) String actionID) {
		return new ModelAndView("action/info")
			.addObject("action", actionService.getInfo(actionID));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(@ModelAttribute Action action) {
		return new ModelAndView("jsonView")
			.addObject("saved", actionService.create(action));
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(@ModelAttribute Action action) {
		return new ModelAndView("jsonView")
			.addObject("saved", actionService.update(action));
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView deleteActions(@RequestParam(required=false) String groupID, @RequestParam(required=false) String actionID) {
		return new ModelAndView("jsonView")
			.addObject("saved" , actionService.deleteActions(groupID, ifEmpty(actionID, "").split(",")) > 0);
	}
}