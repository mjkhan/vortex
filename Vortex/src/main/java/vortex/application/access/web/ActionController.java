package vortex.application.access.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.access.service.ActionService;

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
	
	@RequestMapping(value="/update.do", method=RequestMethod.GET)
	public String updateAction() {
		return "action/update";
	}

	@RequestMapping(value="/update.do", method=RequestMethod.POST)
	public ModelAndView updateAction(@RequestParam String oldPath, @RequestParam String newPath) {
		int affected = actionService.updateAction(oldPath, newPath);
		return new ModelAndView("jsonView")
			.addObject("affected", affected)
			.addObject("saved", affected > 0);
	}
}