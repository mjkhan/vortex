package vortex.application.code.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.code.service.Code;
import vortex.application.code.service.CodeService;
import vortex.application.group.Group;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/code")
public class CodeController extends ApplicationController {
	@Resource(name="codeService")
	private CodeService codeService;
	
	@RequestMapping("/group.do")
	public ModelAndView searchGroups(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("start", req.number("start").intValue())
		   .set("fetch", properties.getInt("fetch"));
		return new ModelAndView(!req.bool("ajax") ? "code/group/list" : "jsonView")
			.addAllObjects(codeService.searchGroups(req)); 
	}
	
	@RequestMapping("/group/info.do")
	public ModelAndView getInfo(@RequestParam(required=false) String groupID) {
		return new ModelAndView("code/group/info")
			.addObject("group", codeService.getInfo(groupID)); 
	}
	
	@RequestMapping("/group/create.do")
	public ModelAndView create(@ModelAttribute Group group) {
		return new ModelAndView("jsonView")
			.addObject("saved", codeService.create(group))
			.addObject("groupID", group.getId());
	}
	
	@RequestMapping("/group/update.do")
	public ModelAndView update(@ModelAttribute Group group) {
		return new ModelAndView("jsonView")
			.addObject("saved", codeService.update(group));
	}
	
	@RequestMapping("/group/delete.do")
	public ModelAndView delete(@RequestParam String groupID) {
		return new ModelAndView("jsonView")
			.addObject("saved", codeService.deleteGroups(groupID.split(",")) > 0);
	}
	
	@RequestMapping("/list.do")
	public ModelAndView getCodes(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		String groupID = req.string("groupID");
		boolean init = isEmpty(groupID);
		ModelAndView mav = new ModelAndView(init ? "code/list" : "jsonView");
		if (init) {
			List<DataObject> groups = codeService.searchGroups(req).value("groups");
			mav.addObject("groups", groups);
			if (!groups.isEmpty()) {
				groupID = groups.get(0).string("grp_id");
				req.set("groupID", groupID);
			}
		}
		return mav.addObject("codes", codeService.getCodes(req).value("codes"));
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getInfo(@RequestParam String groupID, @RequestParam(required=false) String code) {
		return new ModelAndView("code/info")
			.addObject("code", codeService.getInfo(groupID, code));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(@ModelAttribute Code code) {
		return new ModelAndView("jsonView")
			.addObject("saved", codeService.create(code));
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(@ModelAttribute Code code) {
		return new ModelAndView("jsonView")
			.addObject("saved", codeService.update(code));
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView delete(@RequestParam String groupID, @RequestParam String code) {
		return new ModelAndView("jsonView")
			.addObject("saved", codeService.deleteCodes(groupID, code.split(",")) > 0);
	}
}