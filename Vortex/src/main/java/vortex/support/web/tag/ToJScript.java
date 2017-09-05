package vortex.support.web.tag;

import vortex.support.AbstractObject;

public class ToJScript extends AbstractObject {
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