package vortex.application.code.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.code.service.Code;
import vortex.application.code.service.CodeService;
import vortex.application.group.Group;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/code")
public class CodeController extends ApplicationController {
	@Autowired
	private CodeService codeService;
	
	@RequestMapping("/group/list.do")
	public ModelAndView searchGroups(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		BoundedList<DataObject> groups = codeService.searchGroups(
			req.set("start", req.number("start").intValue())
			   .set("fetch", properties.getInt("fetch"))
		);
		ModelAndView mv = new ModelAndView(!req.bool("ajax") ? "code/common-codes" : "jsonView")
			.addObject("groups", groups)
			.addObject("totalGroups", groups.getTotalSize())
			.addObject("groupStart", req.get("start"))
			.addObject("fetch", req.get("fetch"));
		if (!groups.isEmpty()) {
			BoundedList<DataObject> codes = codeService.getCodes(
				req.set("groupID", groups.get(0).get("grp_id"))
				   .set("start", 0)
			);
			mv.addObject("codes", codes)
			  .addObject("totalCodes", codes.getTotalSize())
			  .addObject("codeStart", 0);
		}
		return mv; 
	}
	
	@RequestMapping("/group/info.do")
	public ModelAndView getInfo(@RequestParam(required=false) String groupID) {
		return new ModelAndView("code/group-info")
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
		return searchCodes(request(hreq).set("viewName", "jsonView"));
	}
	
	@RequestMapping("/select.do")
	public ModelAndView select(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		String viewName = req.bool("init") ? "code/select" : "jsonView";
		return searchCodes(req.set("viewName", viewName));
	}
	
	private ModelAndView searchCodes(DataObject req) {
		BoundedList<DataObject> codes = codeService.getCodes(
			req.set("start", req.number("start").intValue())
			   .set("fetch",  properties.getInt("fetch"))
		);
		return new ModelAndView(req.string("viewName"))
			.addObject("codes", codes)
			.addObject("totalCodes", codes.getTotalSize())
			.addObject("codeStart", req.get("start"))
			.addObject("fetch", req.get("fetch"));
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getInfo(@RequestParam String groupID, @RequestParam(required=false) String code) {
		return new ModelAndView("code/code-info")
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