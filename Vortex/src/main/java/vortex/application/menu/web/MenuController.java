package vortex.application.menu.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vortex.application.ApplicationController;
import vortex.application.menu.service.MenuService;

@Controller
@RequestMapping("/menu")
public class MenuController extends ApplicationController {
	@Resource(name="menuService")
	private MenuService menuService;
	
	@RequestMapping("/tree.do")
	public ModelAndView getTree() {
		return modelAndView("menu/tree").addObject("tree", menuService.getTree());
	}
}