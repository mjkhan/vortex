package vortex.application.access.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.access.service.ActionService;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/action")
public class ActionController extends ApplicationController {
	@Resource(name="actionService")
	private ActionService actionService;
	
	@RequestMapping("/select.do")
	public ModelAndView select(@RequestParam(required=false) String prefix) {
		boolean init = isEmpty(prefix);
		ModelAndView mv = new ModelAndView(init ? "action/select" : "jsonView");
		if (init) {
			List<String> prefixes = actionService.getPrefixes();
			prefix = prefixes.get(0);
			mv.addObject("prefixes", prefixes);
		}
		return mv
			.addObject("actions", actionService.getActions(prefix));
	}
	
	@RequestMapping("/group/select.do")
	public ModelAndView selectGroup(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		List<DataObject> groups = actionService.getGroups(
			req.set("start", req.number("start").intValue())
			   .set("fetch", properties.getInt("fetch"))
		);
		return new ModelAndView(req.bool("init") ? "action/group-select" : "jsonView")
			.addObject("groups", groups);
	}

/*	
	@RequestMapping("/group/list.do")
	public ModelAndView getGroups(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		List<DataObject> groups = actionService.getGroups(
			req.set("start", req.number("start").intValue())
			   .set("fetch", properties.getInt("fetch"))
		);
		ModelAndView mv = new ModelAndView(!req.bool("ajax") ? "action/actions" : "jsonView")
			.addObject("groups", groups);
		if (!groups.isEmpty()) {
			mv.addObject("actions", actionService.getActions(groups.get(0).string("grp_id")));
		}
		return mv;
	}
	
	@RequestMapping("/group/info.do")
	public ModelAndView getGroupInfo(@RequestParam(required=false) String groupID) {
		return new ModelAndView("action/group-info")
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
	
	@RequestMapping("/list.do")
	public ModelAndView getActions(@RequestParam(required=false) String groupID) {
		return search(groupID, "jsonView");
	}

	private ModelAndView search(String groupID, String viewName) {
		ModelAndView mav = new ModelAndView(viewName);
		return mav.addObject("actions", actionService.getActions(groupID));
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getInfo(@RequestParam(required=false) String actionID) {
		return new ModelAndView("action/action-info")
			.addObject("action", actionService.getInfo(actionID));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(@ModelAttribute Action action) {
		return new ModelAndView("jsonView")
			.addObject("saved", actionService.create(action))
			.addObject("actionID", action.getId());
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(@ModelAttribute Action action) {
		return new ModelAndView("jsonView")
			.addObject("saved", actionService.update(action));
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView deleteActions(@RequestParam String actionID) {
		return new ModelAndView("jsonView")
			.addObject("saved" , actionService.delete(ifEmpty(actionID, "").split(",")) > 0);
	}
*/
}