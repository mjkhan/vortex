package vortex.support.web.tag;

import javax.servlet.jsp.JspException;

public class ScriptTag extends VortexTag {
	private static final long serialVersionUID = 1L;

	private String attr;
	private boolean write;
	
	public void setAttr(String attr) {
		this.attr = attr;
	}
	
	public void setWrite(boolean write) {
		this.write = write;
	}
	
	@Override
	public int doStartTag() throws JspException {
		try {
			if (!write) {
				setScript();
			} else
				writeScript();
			return SKIP_BODY;
		} catch (Exception e) {
			throw jspException(e);
		}
	}

	private String getScript() {
		return ifEmpty((String)hreq().getAttribute(attr), "");
	}
	
	private void setScript() {
	}
	
	private void setScript(String script) {
		hreq().setAttribute(attr, getScript() + script);
	}
	
	private void writeScript() throws Exception {
		out().write(getScript());
	}
	
	@Override
	public void release() {
		write = false;
		attr = null;
		super.release();
	}
}