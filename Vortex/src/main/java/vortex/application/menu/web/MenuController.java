package vortex.application.menu.web;

import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.menu.Menu;
import vortex.application.menu.service.MenuService;
import vortex.support.data.hierarchy.Hierarchy;
import vortex.support.data.hierarchy.Stringify;

@Controller
@RequestMapping("/menu")
public class MenuController extends ApplicationController {
	@Resource(name="menuService")
	private MenuService menuService;
	
	@RequestMapping("/tree.do")
	public ModelAndView getTree(HttpServletRequest hreq) {
		boolean reload = "true".equals(hreq.getParameter("reload"));
		return modelAndView(!reload ? "menu/tree" : "jsonView").addObject("menus", toString(menuService.getTree()));
	}
	
	private String toString(Hierarchy<Menu> menus) {
		Collection<Menu> tops = menus.topElements();
		String result = new Stringify<Menu>()
			.beginElement((e, level) -> Stringify.indent("    ", level) + "<li id=\"" + e.getId() + "\">" + e.getName())
			.endElement((e, level) -> "\n" + Stringify.indent("    ", level) + "</li>\n")
			.beginChildren((e, level) -> "\n" + Stringify.indent("    ", level) + "<ul>\n")
			.endChildren((e, level) -> Stringify.indent("    ", level) + "</ul>")
			.toString(tops);
		if (!isEmpty(result))
			result = "<ul>\n" + result + "\n</ul>";
		result = "<ul><li id=\"00000\">Vortex 메뉴\n" + result + "\n</li>\n</ul>";
		return result;
	}
	
	@RequestMapping("/info.do")
	public ModelAndView getMenu(HttpServletRequest hreq) {
		String menuID = hreq.getParameter("menuID");
		return modelAndView("menu/info").addObject("menu", menuService.getMenu(menuID));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(HttpServletRequest hreq) {
		String parentID = hreq.getParameter("parentID"),
			   menuName = hreq.getParameter("menuName"),
			   actionPath = hreq.getParameter("actionPath"),
			   imgCfg = hreq.getParameter("imgCfg");
		Menu menu = new Menu();
		menu.setParentID(parentID);
		menu.setName(menuName);
		menu.setActionPath(actionPath);
		menu.setImageConfig(imgCfg);
		String menuID = menuService.create(menu);
		return modelAndView("jsonView")
			.addObject("saved", true)
			.addObject("menuID", menuID);
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(HttpServletRequest hreq) {
		String menuID = hreq.getParameter("menuID"),
			   menuName = hreq.getParameter("menuName"),
			   actionPath = hreq.getParameter("actionPath"),
			   imgCfg = hreq.getParameter("imgCfg");
		Menu menu = menuService.getMenu(menuID);
		menu.setName(menuName);
		menu.setActionPath(actionPath);
		menu.setImageConfig(imgCfg);
		boolean saved = menuService.update(menu);
		return modelAndView("jsonView")
			.addObject("saved", saved);
	}
	
	@RequestMapping("/move.do")
	public ModelAndView move(HttpServletRequest hreq) {
		String menuID = hreq.getParameter("menuID"),
			   parentID = hreq.getParameter("parentID");
		boolean saved = menuService.move(parentID, menuID.split(","));
		return modelAndView("jsonView")
			.addObject("saved", saved);
	}
	
	@RequestMapping("/reorder.do")
	public ModelAndView reorder(HttpServletRequest hreq) {
		String menuID = hreq.getParameter("menuID"),
			   parentID = hreq.getParameter("parentID");
		int offset = Integer.parseInt(ifEmpty(hreq.getParameter("offset"), "0"));
		boolean saved = menuService.reorder(parentID, menuID, offset);
		return modelAndView("jsonView")
			.addObject("saved", saved);
	}
	
	@RequestMapping("/setStatus.do")
	public ModelAndView setStatus(HttpServletRequest hreq) {
		String menuID = hreq.getParameter("menuID"),
			   status = hreq.getParameter("status");
		boolean saved = menuService.setStatus(status, menuID.split(","));
		return modelAndView("jsonView")
			.addObject("saved", saved);
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView delete(HttpServletRequest hreq) {
		String menuID = hreq.getParameter("menuID");
		boolean saved = menuService.delete(menuID.split(","));
		return modelAndView("jsonView")
			.addObject("saved", saved);
	}
}