package vortex.application.menu.web;

import javax.servlet.jsp.JspException;

import vortex.application.User;
import vortex.application.menu.Menu;
import vortex.application.menu.MenuContext;
import vortex.support.web.tag.VortexTag;

public class GrantedTag extends VortexTag {
	private static final long serialVersionUID = 1L;
	private String
		permission,
		var;
	private Menu menu;

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public void setVar(String var) {
		this.var = var;
	}
	@Override
	public int doStartTag() throws JspException {
		if (!isEmpty(permission))
			return processPermission();
		if (menu != null)
			return processMenu();
		return SKIP_BODY;
	}
	
	private int processPermission() throws JspException {
		boolean granted = User.current().isGranted(permission);
		if (!isEmpty(var))
			pageContext.setAttribute(var, granted);
		return granted ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}
	
	private int processMenu() throws JspException {
		MenuContext mctx = (MenuContext)hreq().getAttribute("menuContext");
		String action = mctx.getPermittedAction(menu);
		boolean granted = !isEmpty(action);
		if (!isEmpty(var))
			pageContext.setAttribute(var, granted);
		if (!granted) return SKIP_BODY;
/*
		pageContext.setAttribute("menuID", menu.getId());
		pageContext.setAttribute("menuName", menu.getName());
		pageContext.setAttribute("menuAction", action);
		pageContext.setAttribute("menuImage", menu.getImageConfig());
		String clientAction = Access.current().getAction();
		pageContext.setAttribute("currentMenu",
			action.equals(clientAction) ||
			mctx.hasAction(menu.getChildren(), clientAction)
		);
*/
		return EVAL_BODY_INCLUDE;
	}
	@Override
	public void release() {
/*		
		pageContext.removeAttribute("menuID");
		pageContext.removeAttribute("menuName");
		pageContext.removeAttribute("menuAction");
		pageContext.removeAttribute("menuImage");
		pageContext.removeAttribute("currentMenu");
*/		
		menu = null;
		permission = var = null;
		super.release();
	}
}