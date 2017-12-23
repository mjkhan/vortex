package vortex.application.menu.web;

import javax.servlet.jsp.JspException;

import vortex.application.menu.Menu;
import vortex.application.menu.MenuContext;
import vortex.support.web.tag.VortexTag;

public class PermittedTag extends VortexTag {
	private static final long serialVersionUID = 1L;
	private Menu menu;

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	@Override
	public int doStartTag() throws JspException {
		MenuContext mctx = (MenuContext)hreq().getAttribute("menuContext");
		String action = mctx.getPermittedAction(menu);
		boolean permitted = !isEmpty(action);
		if (permitted) {
			pageContext.setAttribute("menuID", menu.getId());
			pageContext.setAttribute("menuName", menu.getName());
			pageContext.setAttribute("menuAction", action);
			pageContext.setAttribute("menuImage", menu.getImageConfig());
		}
		return permitted ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}
	@Override
	public void release() {
		pageContext.removeAttribute("menuID");
		pageContext.removeAttribute("menuName");
		pageContext.removeAttribute("menuAction");
		pageContext.removeAttribute("menuImage");
		menu = null;
		super.release();
	}
}