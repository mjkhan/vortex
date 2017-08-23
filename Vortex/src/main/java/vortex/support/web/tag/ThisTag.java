package vortex.support.web.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

public class ThisTag extends VortexTag {
	private static final long serialVersionUID = 1L;

	private String
		type,
		var;
	private boolean parent;

	public void setType(String type) {
		this.type = type;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setParent(boolean parent) {
		this.parent = parent;
	}
	@Override
	public int doStartTag() throws JspException {
		try {
			type = ifEmpty(type, "page");
			String value = null;

			if ("page".equalsIgnoreCase(type)) value = thisPage();
			else if ("host".equalsIgnoreCase(type)) value = host();
			else if ("context".equalsIgnoreCase(type)) value = thisContext();
			else if ("baseUrl".equalsIgnoreCase(type)) value = baseUrl();

			if (!isEmpty(value)) {
				if (!isEmpty(var))
					pageContext.setAttribute(var, value);
				else
					out().print(value);
			}
			return SKIP_BODY;
		} catch (Exception e) {
			throw jspException(e);
		}
	}

	private String thisPage() {
		if (parent) {
			return hreq().getRequestURI().replace(hreq().getContextPath(), "");
		} else {
			String includedPath = (String)hreq().getAttribute("javax.servlet.include.servlet_path");
			return isEmpty(includedPath) ? hreq().getRequestURI().replace(hreq().getContextPath(), "") : includedPath;
		}
	}

	private String host() {
		HttpServletRequest request = hreq();
		String server = request.getServerName(),
			   port = ":" + request.getServerPort();
		if (request.getRequestURL().toString().indexOf(port) > -1)
			server += port;
		return request.getScheme() + "://" + server;
	}

	private String baseUrl() {
		return hreq().getScheme() + "://"
			 + hreq().getServerName()
			 + (hreq().getServerPort() == 80 ? "" : ":" + hreq().getServerPort())
			 + hreq().getContextPath();
	}

	private String thisContext() {
		return host() + hreq().getContextPath();
	}
	@Override
	public void release() {
		type = var = null;
		parent = false;
		super.release();
	}
}