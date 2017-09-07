package vortex.application.code.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@RequestMapping("/group/list.do")
	public ModelAndView getGroups(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("start", req.number("start"))
		   .set("fetch", properties.getInt("fetch"));
		return modelAndView(!req.bool("ajax") ? "code/group/list" : "jsonView", codeService.getGroups(req)); 
	}
	
	@RequestMapping("/group/info.do")
	public ModelAndView getGroup(HttpServletRequest hreq) {
		return modelAndView("code/group/info", codeService.getGroup(request(hreq))); 
	}
	
	@RequestMapping("/group/create.do")
	public ModelAndView createGroup(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Group group = new Group();
		group.setId(req.string("groupID"));
		group.setName(req.string("groupName"));
		group.setDescription(req.string("description"));
		return modelAndView("jsonView", codeService.createGroup(req.set("group", group)));
	}
	
	@RequestMapping("/group/update.do")
	public ModelAndView updateGroup(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Group group = codeService.getGroup(req).value("group");
		group.setName(req.string("groupName"));
		group.setDescription(req.string("description"));
		return modelAndView("jsonView", codeService.updateGroup(req.set("group", group)));
	}
	
	@RequestMapping("/group/delete.do")
	public ModelAndView deleteGroup(HttpServletRequest hreq) {
		return modelAndView("jsonView", codeService.deleteGroups(request(hreq)));
	}
	
	@RequestMapping("/list.do")
	public ModelAndView getCodes(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		String groupID = req.string("groupID");
		boolean init = isEmpty(groupID);
		ModelAndView mav = new ModelAndView(init ? "code/list" : "jsonView");
		if (init) {
			List<DataObject> groups = codeService.getGroups(req).value("groups");
			mav.addObject("groups", groups);
			if (!groups.isEmpty()) {
				groupID = groups.get(0).string("grp_id");
				req.set("groupID", groupID);
			}
		}
		return mav.addObject("codes", codeService.getCodes(req).value("codes"));
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getCode(HttpServletRequest hreq) {
		return modelAndView("code/info", codeService.getCode(request(hreq)));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView createCode(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Code code = new Code();
		code.setGroupID(req.string("groupID"));
		code.setCode(req.string("code"));
		code.setValue(req.string("value"));
		code.setDescription(req.string("description"));
		return modelAndView("jsonView", codeService.createCode(req.set("code", code)));
	}
	
	@RequestMapping("/update.do")
	public ModelAndView updateCode(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		Code code = codeService.getCode(req).value("code");
		code.setValue(req.string("value"));
		code.setDescription(req.string("description"));
		return modelAndView("jsonView", codeService.updateCode(req.set("code", code)));
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView deleteCodes(HttpServletRequest hreq) {
		return modelAndView("jsonView", codeService.deleteCodes(request(hreq)));
	}
}