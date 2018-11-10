package vortex.support.web.tag;

import vortex.support.AbstractComponent;

public class ToJScript extends AbstractComponent {
	public static final String get(String s) {
		return isEmpty(s) ? s :
			s.replace("\\","\\\\")
			 .replace("\r","\\r")
			 .replace("\n","\\n")
			 .replace("\t","\\t")
			 .replace("\"","\\\"")
			 .replace("/script", "/scr\" + \"ipt")
			 .replace("/SCRIPT", "/SCR\" + \"IPT");
	}
}