package vortex.application;

import vortex.support.AbstractObject;

public class Client extends AbstractObject {
	private static final ThreadLocal<Client> current = new ThreadLocal<>();
	
	private String
		action,
		ipAddress;

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
		return current.get();
	}
	
	public Client setCurrent() {
		current.set(this);
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