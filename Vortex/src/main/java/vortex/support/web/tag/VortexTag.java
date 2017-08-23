package vortex.support.web.tag;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import vortex.support.Assert;

public class VortexTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;

	public final HttpServletRequest hreq() {
		return HttpServletRequest.class.cast(pageContext.getRequest());
	}

	public final HttpServletResponse hresp() {
		return HttpServletResponse.class.cast(pageContext.getResponse());
	}

	protected JspWriter out() {
		return pageContext.getOut();
	}

	protected void writeBody() throws JspException {
		try {
			BodyContent content = getBodyContent();
			content.getEnclosingWriter().print(content.getString());
			content.clearBody();
		} catch (Exception e) {
			throw jspException(e);
		}
	}
	@Override
	public int doEndTag() throws JspException {
		release();
		return super.doEndTag();
	}

	protected static final boolean equals(Object lv, Object rv) {
		return lv == rv ? true : lv != null && lv.equals(rv);
	}

	protected static final boolean isEmpty(Object obj) {
		return Assert.isEmpty(obj);
	}

	protected static final <T> T ifEmpty(T t, T nt) {
		return Assert.ifEmpty(t, nt);
	}

	protected static final <T> T ifEmpty(T t, Supplier<T> nt) {
		return Assert.ifEmpty(t, nt);
	}

	protected static final <T> T notEmpty(T t, String name) {
		return Assert.notEmpty(t, name);
	}

	protected static final JspException jspException(Throwable t) {
		if (t instanceof RuntimeException) throw RuntimeException.class.cast(t);

		return t instanceof JspException ? JspException.class.cast(t) : new JspException(t);
	}
}
