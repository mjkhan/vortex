package vortex.application.menu.web;

import java.util.function.Function;

import vortex.application.menu.Menu;
import vortex.support.AbstractObject;

public class MenuWriter extends AbstractObject {
	private static final String TAG = "<li id='{id}'><a onclick=\"{action}\">{name}</a>{children}</li>";
	
	private Function<Menu, String> writer;
	
	public MenuWriter setWriter(Function<Menu, String> writer) {
		this.writer = writer;
		return this;
	}
	
	public MenuWriter write(Menu menu) {
		return this;
	}
}