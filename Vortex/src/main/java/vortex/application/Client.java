package vortex.application;

import vortex.support.AbstractObject;

public class Client extends AbstractObject {
	private static final ThreadLocal<Client> current = new ThreadLocal<>();
	
	private String
		action,
		ipAddress;
	private boolean
		newSession,
		mobile;

	public String getAction() {
		return action;
	}

	public Client setAction(String action) {
		this.action = action;
		return this;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public Client setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		return this;
	}
	
	public static Client current() {
		return ifEmpty(current.get(), Client::new);
	}
	
	public Client setCurrent() {
		current.set(this);
		return this;
	}
	
	public boolean inNewSession() {
		return newSession;
	}
	
	public Client setNewSession(boolean newSession) {
		this.newSession = newSession;
		return this;
	}
	
	public boolean isMobile() {
		return mobile;
	}
	
	public Client setMobile(boolean mobile) {
		this.mobile = mobile;
		return this;
	}
	
	public static Client release() {
		Client client = current.get();
		current.remove();
		return client;
	}
	@Override
	public String toString() {
		return String.format("%s('%s')", getClass().getSimpleName(), getIpAddress());
	}
}