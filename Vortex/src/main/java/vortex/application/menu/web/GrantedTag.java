package vortex.application.menu.web;

import javax.servlet.jsp.JspException;

import vortex.application.Client;
import vortex.application.User;
import vortex.application.menu.Menu;
import vortex.application.menu.MenuContext;
import vortex.support.web.tag.VortexTag;

public class GrantedTag extends VortexTag {
	private static final long serialVersionUID = 1L;
	private String permission;
	private Menu menu;

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	public void setMenu(Menu menu) {
		this.menu = menu;
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
		return User.current().getPermissionIDs().contains(permission) ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}
	
	private int processMenu() throws JspException {
		MenuContext mctx = (MenuContext)hreq().getAttribute("menuContext");
		String action = mctx.getPermittedAction(menu);
		if (isEmpty(action)) return SKIP_BODY;

		pageContext.setAttribute("menuID", menu.getId());
		pageContext.setAttribute("menuName", menu.getName());
		pageContext.setAttribute("menuAction", action);
		pageContext.setAttribute("menuImage", menu.getImageConfig());
		String clientAction = Client.current().getAction();
		pageContext.setAttribute("currentMenu",
			action.equals(clientAction) ||
			mctx.hasAction(menu.getChildren(), clientAction)
		);
		return EVAL_BODY_INCLUDE;
	}
	@Override
	public void release() {
		pageContext.removeAttribute("menuID");
		pageContext.removeAttribute("menuName");
		pageContext.removeAttribute("menuAction");
		pageContext.removeAttribute("menuImage");
		pageContext.removeAttribute("currentMenu");
		menu = null;
		permission = null;
		super.release();
	}
}