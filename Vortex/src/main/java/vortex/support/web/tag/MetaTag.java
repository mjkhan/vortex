package vortex.support.web.tag;

import javax.servlet.jsp.JspException;

import vortex.support.data.BoundedList;

public class MetaTag extends VortexTag {
	private static final long serialVersionUID = 1L;
	
	private BoundedList<?> list;

	private String
		info,
		var;
	
	public void setData(BoundedList<?> data) {
		this.list = data;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setVar(String var) {
		this.var = var;
	}
	@Override
	public int doStartTag() throws JspException {
		printInfo();
		return SKIP_BODY;
	}

	protected void printInfo() throws JspException {
		if (isEmpty(info)) return;

		try {
			printListInfo();
		} catch (Exception e) {
			throw jspException(e);
		}
	}

	private void print(int i) throws Exception {
		if (!isEmpty(var))
			pageContext.setAttribute(var, Integer.valueOf(i));
		else
			out().print(i);
	}

	private void print(boolean b) throws Exception {
		if (!isEmpty(var))
			pageContext.setAttribute(var, Boolean.valueOf(b));
		else
			out().print(b);
	}

	private void printListInfo() throws Exception {
		if ("size".equals(info))
			print(list == null ? 0 : list.size());
		else if ("totalSize".equals(info))
			print(list == null ? 0 : list.getTotalSize());
		else if ("hasMore".equals(info))
			print(list != null && list.hasMore());
		else if ("hasPrevious".equals(info))
			print(list != null && list.hasPrevious());
		else if ("hasNext".equals(info))
			print(list != null && list.hasNext());
		else if ("start".equals(info))
			print(list == null ? -1 : list.getStart());
		else if ("end".equals(info))
			print(list == null ? -1 : list.getEnd());
	}
	@Override
	public void release() {
		list = null;
		info = var = null;
		super.release();
	}
}