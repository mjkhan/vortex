package vortex.support.data;

import vortex.support.Assert;

public enum Status {
	CREATED("000"),
	ACTIVE("001"),
	INACTIVE("998"),
	REMOVED("999");
	
	private final String code;
	
	private Status(String code) {
		this.code = code;
	}
	
	public String code() {
		return code;
	}
	
	public static Status codeOf(String code) {
		if (Assert.isEmpty(code))
			return null;
		for (Status status: values()) {
			if (code.equals(status.code))
				return status;
		}
		throw new IllegalArgumentException("Invalid Status code:" + code);
	}
}