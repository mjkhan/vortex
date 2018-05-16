package vortex.support.util;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import vortex.support.AbstractObject;

public class StringEncoder extends AbstractObject {
	public static final String hash(String s) {
		if (isEmpty(s)) return "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(s.getBytes("UTF-8"), 0, s.length());
			return DatatypeConverter.printHexBinary(md.digest());
		} catch (Exception e) {
			throw runtimeException(e);
		}
	}
}