package vortex.support.web;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import vortex.support.AbstractComponent;

public class ClientAddress extends AbstractComponent {
	private static final String
		UNKNOWN = "unknown",
		LOCALHOST_V4 = "127.0.0.1",
		LOCALHOST_V6 = "0:0:0:0:0:0:0:1";
	private static final String[] HEADERS = {"x-forwarded-for", "X-FORWARDED-FOR", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR"};
	
	public static final String getLocalAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			throw runtimeException(e);
		}
	}
	
	public static final String get(HttpServletRequest hreq) {
		for (String header: HEADERS) {
			String addr = hreq.getHeader(header);
			if (!isEmpty(addr) && !UNKNOWN.equalsIgnoreCase(addr))
				return addr;
		}
		
		String addr = hreq.getRemoteAddr();
		if (LOCALHOST_V4.equals(addr) || LOCALHOST_V6.equals(addr))
		try {
			addr = getLocalAddress();
		} catch (Exception e) {
			throw runtimeException(e);
		}

		return addr;
	}
	private ClientAddress() {}
}