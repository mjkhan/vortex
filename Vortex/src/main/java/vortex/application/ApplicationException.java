package vortex.application;

import vortex.support.data.DataObject;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private DataObject objs;
	
	public DataObject info() {
		if (objs == null)
			objs = new DataObject();
		return objs;
	}
	
	public ApplicationException info(String key, Object value) {
		info().set(key, value);
		return this;
	}
}
