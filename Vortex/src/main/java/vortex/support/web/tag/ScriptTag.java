package vortex.support.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

public class ScriptTag extends VortexTag {
	private static final long serialVersionUID = 1L;

	private String type;
	private boolean write;
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setWrite(boolean write) {
		this.write = write;
	}
	
	@Override
	public int doStartTag() throws JspException {
		if (!write)
			return EVAL_BODY_BUFFERED;
		try {
			writeScript();
			return SKIP_PAGE;
		} catch (Exception e) {
			throw jspException(e);
		}
	}
	
	@Override
	public int doAfterBody() throws JspException {
		if (write)
			return SKIP_PAGE;
		try {
			BodyContent content = getBodyContent();
			String str = content != null ? content.getString() : null;
			if (!isEmpty(str)) {
				setScript(str.trim());
			}
			return SKIP_BODY;
		} catch (Exception e) {
			throw jspException(e);
		}
	}

	private String getScript() {
		return ifEmpty((String)hreq().getAttribute(type), "");
	}
	
	private void setScript(String script) {
		hreq().setAttribute(type, getScript() + script);
	}
	
	private void writeScript() throws Exception {
		out().write(getScript());
	}
	
	@Override
	public void release() {
		write = false;
		type = null;
		super.release();
	}
}