package vortex.support.web.tag;

import java.text.DateFormat;

import javax.servlet.jsp.JspException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONTag extends VortexTag {
	private static final long serialVersionUID = 1L;

	private Object data;
	private String var;
	private ObjectMapper mapper;
	private DateFormat dateFormat;

	public void setData(Object data) {
		this.data = data;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}
	
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	private static final String KEY = "jsonMapper";
	
	private ObjectMapper objectMapper() {
		if (this.mapper != null)
			return this.mapper;

		ObjectMapper mapper = (ObjectMapper)hreq().getAttribute(KEY);
		if (mapper == null)
			hreq().setAttribute(KEY, mapper = new ObjectMapper());
		if (dateFormat != null)
			mapper.setDateFormat(dateFormat);
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
		mapper = null;
		dateFormat = null;
		data = var = null;
		super.release();
	}
}