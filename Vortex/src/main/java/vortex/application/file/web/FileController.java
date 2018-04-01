package vortex.application.file.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.file.File;
import vortex.application.file.service.FileService;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

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
	
	@RequestMapping("/upload.do")
	public ModelAndView upload(@RequestParam MultipartFile upload) {
		String[] fileIDs = fileService.create(upload);
		String fileID = !isEmpty(fileIDs) ? fileIDs[0] : null;
		return new ModelAndView("jsonView")
			.addObject("saved", !isEmpty(fileID))
			.addObject("fileID", fileID);
	}
	
	@RequestMapping("/multiUpload.do")
	public ModelAndView upload(@RequestParam MultipartFile[] uploads) {
		String[] fileIDs = fileService.create(uploads);
		return new ModelAndView("jsonView")
			.addObject("saved", !isEmpty(fileIDs));
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(@ModelAttribute File file) {
		return new ModelAndView("jsonView")
			.addObject("saved", fileService.update(file));
	}
	
	public ModelAndView remove(@RequestParam String fileID) {
		int affected = fileService.remove(fileID.split(","));
		return new ModelAndView("jsonView")
			.addObject("affected", affected)
			.addObject("saved", affected > 0);
	}
}