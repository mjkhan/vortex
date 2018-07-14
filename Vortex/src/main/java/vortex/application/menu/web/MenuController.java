package vortex.application.menu.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.menu.Menu;
import vortex.application.menu.service.MenuService;
import vortex.support.data.hierarchy.Hierarchy;
import vortex.support.data.hierarchy.Stringify;

@Controller("menuController")
@RequestMapping("/menu")
public class MenuController extends ApplicationController {
	@Autowired
	private MenuService menuService;
	
	@RequestMapping("/tree.do")
	public ModelAndView getTree(HttpServletRequest hreq) {
		boolean reload = "true".equals(hreq.getParameter("reload"));
		return new ModelAndView(!reload ? "menu/tree" : "jsonView")
			.addObject("menus", toString(menuService.getTree()));
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
	public ModelAndView getInfo(@RequestParam(required=false) String menuID) {
		return new ModelAndView("menu/info")
			.addObject("menu", menuService.getMenu(menuID));
	}
	
	@RequestMapping("/create.do")
	public ModelAndView create(@ModelAttribute Menu menu) {
		return new ModelAndView("jsonView")
			.addObject("saved", menuService.create(menu))
			.addObject("menuID", menu.getId());
	}
	
	@RequestMapping("/update.do")
	public ModelAndView update(@ModelAttribute Menu menu) {
		return new ModelAndView("jsonView")
			.addObject("saved", menuService.update(menu));
	}
	
	@RequestMapping("/move.do")
	public ModelAndView move(@RequestParam String menuID, @RequestParam String parentID) {
		return new ModelAndView("jsonView")
			.addObject("saved", menuService.move(parentID, menuID.split(",")));
	}
	
	@RequestMapping("/reorder.do")
	public ModelAndView reorder(@RequestParam String menuID, @RequestParam String parentID, @RequestParam int offset) {
		return new ModelAndView("jsonView")
			.addObject("saved", menuService.reorder(parentID, menuID, offset));
	}
	
	@RequestMapping("/setStatus.do")
	public ModelAndView setStatus(@RequestParam String menuID, @RequestParam String status) {
		return new ModelAndView("jsonView")
			.addObject("saved", menuService.setStatus(status, menuID.split(",")));
	}
	
	@RequestMapping("/delete.do")
	public ModelAndView delete(@RequestParam String menuID) {
		return new ModelAndView("jsonView")
			.addObject("saved", menuService.delete(menuID.split(",")));
	}
/*	
	public void setMenuContext(HttpServletRequest hreq) {
		MenuContext mctx = menuService.getMenuContext();
		if (mctx == null) return;
		
		hreq.setAttribute("menuContext", mctx);
		Hierarchy<Menu> menus = mctx.getMenus();
		hreq.setAttribute("menus", menus);
		hreq.setAttribute("topMenus", menus.topElements());
	}
*/
}