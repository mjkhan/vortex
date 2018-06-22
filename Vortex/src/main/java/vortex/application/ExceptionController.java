package vortex.application;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController extends ApplicationController {
	@ExceptionHandler(ApplicationException.class)
	public ModelAndView onApplicationException(ApplicationException e) {
		Throwable cause = rootCause(e);
		String stacktrace = ExceptionUtils.getStackTrace(cause);
		String errorCode = e.getCode(),
			   msg = "";
		
		return new ModelAndView("jsonView")
			.addObject("exception", cause)
			.addObject("stacktrace", stacktrace)
			.addObject("code", errorCode)
			.addObject("message", msg);
	}
	
	@ExceptionHandler(Throwable.class)
	public ModelAndView onException(Exception e) {
		Throwable cause = rootCause(e);
		String stacktrace = ExceptionUtils.getStackTrace(cause),
			   errorCode = "errorCode",
			   msg = "";
		return new ModelAndView("jsonView")
			.addObject("exception", cause)
			.addObject("stacktrace", stacktrace)
			.addObject("code", errorCode)
			.addObject("message", msg);
	}
}