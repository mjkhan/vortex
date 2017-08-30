package vortex.support.web.tag;

import javax.servlet.jsp.JspException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONTag extends VortexTag {
	private Object data;
	private String var;

	public void setData(Object data) {
		this.data = data;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
	private static final String KEY = "jsonMapper";
	
	private ObjectMapper objectMapper() {
		ObjectMapper mapper = (ObjectMapper)hreq().getAttribute(KEY);
		if (mapper == null)
			hreq().setAttribute(KEY, mapper = new ObjectMapper());
		return mapper;
	}
	
	@Override
	public int doStartTag() throws JspException {
		try {
			String s = objectMapper().writeValueAsString(data);
			if (!isEmpty(var))
				pageContext.setAttribute(var, s);
			else
				out().print(s);
			return SKIP_BODY;
		} catch (Exception e) {
			throw jspException(e);
		}
	}
	
	@Override
	public void release() {
		data = var = null;
		super.release();
	}
}