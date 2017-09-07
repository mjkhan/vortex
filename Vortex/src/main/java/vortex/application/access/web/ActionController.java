package vortex.application.access.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
		req.set("start", req.number("start"))
		   .set("fetch", properties.getInt("fetch"));
		return modelAndView(!req.bool("ajax") ? "action/group/list" : "jsonView", actionService.getGroups(req)); 
	}
	
	@RequestMapping("/group/info.do")
	public ModelAndView getGroup(HttpServletRequest hreq) {
		return modelAndView("action/group/info", actionService.getGroup(request(hreq))); 
	}
	
	@RequestMapping("/group/create.do")
	public ModelAndView createGroup(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Group group = new Group();
		group.setId(req.string("groupID"));
		group.setName(req.string("groupName"));
		group.setDescription(req.string("description"));
		return modelAndView("jsonView", actionService.createGroup(req.set("group", group)));
	}
	
	@RequestMapping("/group/update.do")
	public ModelAndView updateGroup(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Group group = actionService.getGroup(req).value("group");
		group.setName(req.string("groupName"));
		group.setDescription(req.string("description"));
		return modelAndView("jsonView", actionService.updateGroup(req.set("group", group)));
	}
	
	@RequestMapping("/group/delete.do")
	public ModelAndView deleteGroup(HttpServletRequest hreq) {
		return modelAndView("jsonView", actionService.deleteGroups(request(hreq)));
	}
	
	@RequestMapping("/list.do")
	public ModelAndView getActions(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		String groupID = req.string("groupID");
		boolean init = isEmpty(groupID);
		ModelAndView mav = new ModelAndView(init ? "action/list" : "jsonView");
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
	public ModelAndView getAction(HttpServletRequest hreq) {
		return modelAndView("action/info", actionService.getAction(request(hreq)));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView createAction(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Action action = new Action();
		action.setGroupID(req.string("groupID"));
		action.setName(req.string("actionName"));
		action.setPath(req.string("actionPath"));
		action.setDescription(req.string("description"));
		return modelAndView("jsonView", actionService.createAction(req.set("action", action)));
	}
	
	@RequestMapping("/update.do")
	public ModelAndView updateCode(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Action action = actionService.getAction(req).value("action");
		action.setName(req.string("actionName"));
		action.setPath(req.string("actionPath"));
		action.setDescription(req.string("description"));
		return modelAndView("jsonView", actionService.updateAction(req.set("action", action)));
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView deleteActions(HttpServletRequest hreq) {
		return modelAndView("jsonView", actionService.deleteActions(request(hreq)));
	}
}