package vortex.application;

import vortex.support.Assert;
import vortex.support.data.DataObject;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public static ApplicationException create(Throwable t) {
		if (t instanceof ApplicationException)
			return ApplicationException.class.cast(t);
		return new ApplicationException(Assert.rootCause(t));
	}
	
	private DataObject objs;
	
	private ApplicationException(Throwable t) {
		Assert.rootCause(t);
	}
	
	public DataObject info() {
		if (objs == null)
			objs = new DataObject();
		return objs;
	}
	
	public ApplicationException info(String key, Object value) {
		info().set(key, value);
		return this;
	}
	
	public String getCode() {
		return objs != null ? objs.string("code") : null;
	}
	
	public ApplicationException setCode(String errorCode) {
		return info("code", errorCode);
	}
	
	@Override
	public String getMessage() {
		return objs != null ? objs.string("msg") : null;
	}
	
	public ApplicationException setMessage(String msg) {
		return info("msg", msg);
	}
}