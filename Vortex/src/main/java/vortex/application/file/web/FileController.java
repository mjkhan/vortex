package vortex.application.file.web;

import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.file.File;
import vortex.application.file.service.FileService;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Controller
@RequestMapping("/file")
public class FileController extends ApplicationController {
	@Resource(name="fileService")
	private FileService fileService;
	
	private ModelAndView search(DataObject req) {
		BoundedList<DataObject> files = fileService.search(
			req.set("start", req.number("start").intValue())
			   .set("fetch", properties.getInt("fetch"))
		);
		return new ModelAndView(req.string("viewName"))
				.addObject("files", files)
				.addObject("totalSize", files.getTotalSize())
				.addObject("start", req.get("start"))
				.addObject("fetch", req.get("fetch"));
	}
	
	@RequestMapping("/list.do")
	public ModelAndView search(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("viewName", req.bool("json") ? "jsonView" : "file/list");
		return search(req);
	}
	
	@RequestMapping("/select.do")
	public ModelAndView select(HttpServletRequest hreq) {
		DataObject req = request(hreq);
		req.set("viewName", req.bool("json") ? "jsonView" : "file/select");
		return search(req);
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getInfo(@RequestParam(required=false) String fileID) {
		return new ModelAndView("file/info")
			.addObject("file", fileService.getInfo(fileID)); 
	}
	
	@RequestMapping(value="/upload.do", method=RequestMethod.GET)
	public ModelAndView upload() {
		return new ModelAndView("file/upload");
	}

	@RequestMapping(value="/upload.do", method=RequestMethod.POST)
	public ModelAndView upload(@RequestParam("upload") MultipartFile[] upload) {
		List<File> files = fileService.create(upload);
		return new ModelAndView("jsonView")
			.addObject("affected", files.size())
			.addObject("saved", !files.isEmpty());
	}
	
	@RequestMapping("/download.do")
	public void download(@RequestParam String fileID, HttpServletResponse hresp) throws Exception {
		hresp.setCharacterEncoding("UTF-8");
		File file = fileService.getFile(fileID);
		if (file == null) {
			hresp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			hresp.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			hresp.setContentType(file.getContentType());
			hresp.setHeader("Content-Disposition", "inline; filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") +"\"");
			hresp.setContentLength((int)file.getSize());
			FileCopyUtils.copy(file.getInputStream(), hresp.getOutputStream());
		}
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(@ModelAttribute File file) {
		return new ModelAndView("jsonView")
			.addObject("saved", fileService.update(file));
	}
	
	@RequestMapping("/copy.do")
	public ModelAndView copy(@RequestParam String fileID) {
		int affected = fileService.copy(fileID.split(","));
		return new ModelAndView("jsonView")
				.addObject("affected", affected)
				.addObject("saved", affected > 0);
	}
	
	@RequestMapping("/remove.do")
	public ModelAndView remove(@RequestParam String fileID) {
		int affected = fileService.remove(fileID.split(","));
		return new ModelAndView("jsonView")
			.addObject("affected", affected)
			.addObject("saved", affected > 0);
	}
}