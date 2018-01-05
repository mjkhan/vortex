package vortex.application.menu.web;

import javax.servlet.jsp.JspException;

import vortex.application.Client;
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
		super.release();
	}
}