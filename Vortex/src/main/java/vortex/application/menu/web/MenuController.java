package vortex.application.menu.web;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;

import vortex.application.ApplicationController;
import vortex.application.menu.service.MenuService;

@RequestMapping("/menu")
public class MenuController extends ApplicationController {
	@Resource(name="menuService")
	private MenuService menuService;
}