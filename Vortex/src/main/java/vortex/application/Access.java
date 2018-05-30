package vortex.application;

import vortex.support.AbstractObject;

public class Access extends AbstractObject {
	private static final ThreadLocal<Access> current = new ThreadLocal<>();
	
	private String
		action,
		ipAddress;
	private boolean
		newSession,
		mobile;

	public String getAction() {
		return action;
	}

	public Access setAction(String action) {
		this.action = action;
		return this;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public Access setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		return this;
	}
	
	public static Access current() {
		return ifEmpty(current.get(), Access::new);
	}
	
	public Access setCurrent() {
		current.set(this);
		return this;
	}
	
	public boolean inNewSession() {
		return newSession;
	}
	
	public Access setNewSession(boolean newSession) {
		this.newSession = newSession;
		return this;
	}
	
	public boolean isMobile() {
		return mobile;
	}
	
	public Access setMobile(boolean mobile) {
		this.mobile = mobile;
		return this;
	}
	
	public static Access release() {
		Access access = current.get();
		current.remove();
		return access;
	}
	@Override
	public String toString() {
		return String.format("%s('%s')", getClass().getSimpleName(), getIpAddress());
	}
}