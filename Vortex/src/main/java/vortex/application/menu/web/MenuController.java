package vortex.application.menu.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.menu.Menu;
import vortex.application.menu.service.MenuService;
import vortex.support.data.hierarchy.Hierarchy;

@RequestMapping("/menu")
public class MenuController extends ApplicationController {
	@Resource(name="menuService")
	private MenuService menuService;
	
	@RequestMapping("/list.do")
	public ModelAndView getMenus(HttpServletRequest hreq, HttpServletResponse hresp) {
		String parentID = hreq.getParameter("parentID");
		boolean init = isEmpty(parentID);
		ModelAndView mav = new ModelAndView(init ? "menu/list" : "jsonView");
		if (init) {
			Hierarchy<Menu> tree = menuService.getTree();
			mav.addObject("tree", tree);
		}
		return mav.addObject("menus", menuService.getMenus(parentID));
	}
}