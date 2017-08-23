package vortex.support.web.tag;

import javax.servlet.jsp.JspException;

import vortex.support.data.BoundedList;

public class PaginateTag extends VortexTag {
	private static final long serialVersionUID = 1L;

	private static final String
		SPACE = "&nbsp;",
		FIRST = "|◀",
	    PREVIOUS = "◀",
	    NEXT = "▶",
	    LAST = "▶|",
	    NULL = "";
	private static final int
		ALL = -1,
		INDENT = 2;
	private BoundedList<?> items;
	private int
	    links = ALL,
		space = INDENT,
		lc = ALL,
		rc = ALL,
        band = ALL,
        page = ALL,
        fetchCount = ALL;
	private String
		attrs,
		before = NULL,
		after = NULL,
		beforeCurrent = NULL,
		current = NULL,
		afterCurrent = NULL,
		first = FIRST,
		previous = PREVIOUS,
		next = NEXT,
		last = LAST,
		info;
	
	public void setData(BoundedList<?> data) {
		this.items = data;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	public void setLinks(int visibleLinks) {
	    if (visibleLinks <= BoundedList.Fetch.ALL) throw new IllegalArgumentException(Integer.toString(visibleLinks));
	    this.links = visibleLinks;
	}

	public void setSpace(int space) {
	    this.space = space;
	}

	public void setBefore(String before){this.before = before;}

	public void setAfter(String after) {this.after = after;}

	public void setBeforeCurrent(String beforeCurrent) {
	    this.beforeCurrent = beforeCurrent;
	}

	public void setCurrent(String current) {
	    this.current = current;
	}

	public void setAfterCurrent(String afterCurrent) {
	    this.afterCurrent = afterCurrent;
	}

	public void setFirst(String first){this.first = first;}

	public void setPrevious(String previous){this.previous = previous;}

	public void setNext(String next){this.next = next;}

	public void setLast(String last){this.last = last;}

	public void setInfo(String info) {this.info = info;}
	@Override
	public int doStartTag() throws JspException {
		rc = items == null ? 0 : items.getTotalSize();
		if (rc < 1) return SKIP_BODY;
		if ((fetchCount = items.getFetchSize()) < 1) return SKIP_BODY;

	    lc = BoundedList.Fetch.count(rc, fetchCount);
	    if (lc < 2) return SKIP_BODY;

		page = BoundedList.Fetch.page(items.getStart(), fetchCount);
		band = BoundedList.Fetch.band(page, links);
		try {
			if (isEmpty(info))
				out().println(getLinkTags());
			else
				writeInfo();
		    return SKIP_BODY;
		} catch (Exception e) {
			throw jspException(e);
		}
	}

	private String getLinkTags() {
	    StringBuilder buffer = new StringBuilder();
	    if (links != ALL) {
	        firstTag(buffer);
	        previousTag(buffer);
	    }
	    visibleLinksTag(buffer);
	    if (links != ALL) {
	        int bandCount = lc / links;
	        bandCount += lc % links == 0 ? 0 : 1;
	        nextTag(buffer, band, bandCount);
	        lastTag(buffer, band, bandCount);
	    }
	    return buffer.toString();
	}

	private void firstTag(StringBuilder buffer) {
	    if (band < 2) return;
		buffer.append(linkTag(first, 0, NULL, NULL));
	    space(buffer);
	}

	private void previousTag(StringBuilder buffer) {
	    if (band < 1) return;

	    int prevBand = band - 1,
			prevPage = (prevBand * links) + (links - 1),
	        fromRec = prevPage * fetchCount;
	    buffer.append(linkTag(previous, fromRec, NULL, NULL));
	    space(buffer);
	}

	private void visibleLinksTag(StringBuilder buffer){
	    int fromPage = links == ALL ? 0 : band * links,
	        toPage = links == ALL ? lc : Math.min(lc, fromPage + links);
	     for (int i = fromPage; i < toPage; ++i) {
	         String thisPage = ifEmpty(current, Integer.toString(i + 1));
	         if (i == page) buffer.append(beforeCurrent + thisPage + afterCurrent);
	         else {
				int fromRec = i * fetchCount;
	            buffer.append(linkTag(thisPage, fromRec, before, after));
	         }
	        if (i < toPage - 1) space(buffer);
	     }
	}

	private void nextTag(StringBuilder buffer, int currentBand, int bandCount) {
		if (bandCount - currentBand < 2) return;

		int band = currentBand + 1,
			page = band * links,
			fromRec = page * fetchCount;
	    space(buffer);
	    buffer.append(linkTag(next, fromRec, NULL, NULL));
	}

	private void lastTag(StringBuilder buffer, int currentBand, int bandCount) {
	    int band = bandCount - 1;
	    if (band - currentBand < 2) return;

	    int page = band * links,
	        fromRec = page * fetchCount;
	    space(buffer);
		buffer.append(linkTag(last, fromRec, NULL, NULL));
	}

	private void space(StringBuilder buffer){for (int i = 0; i < space; ++i) buffer.append(SPACE);}

	private String linkTag(String string, int currentRecord, String before, String after) {
		if ("&nbsp;".equals(string)) return "";

		String inner = attrs.replace("@{start}", Integer.toString(currentRecord));
		inner = inner.replace("@{end}", Integer.toString(BoundedList.Fetch.end(rc, fetchCount, currentRecord)));

		return before + "<a " + inner + ">" + string + "</a>" + after;
	}

	private void writeInfo() throws Exception {
		if ("link-number".equals(info))
			out().println(page + 1);
	}
	@Override
	public void release() {
		items = null;
		attrs = info = null;
		before = beforeCurrent = current = afterCurrent = after = NULL;
		first = FIRST;
		previous = PREVIOUS;
		next = NEXT;
		last = LAST;
		rc = band = page = links = lc = fetchCount = ALL;
		space = INDENT;
		super.release();
	}
}