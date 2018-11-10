package vortex.application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import vortex.support.AbstractComponent;

public class RequestInterceptor extends AbstractComponent implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest hreq, HttpServletResponse hresp, Object handler) throws Exception {
		log().debug(() -> getClass().getSimpleName() + ".preHandle(" + hreq.getRequestURI() + ")");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest hreq, HttpServletResponse hresp, Object handler, ModelAndView modelAndView) throws Exception {
		log().debug(() -> getClass().getSimpleName() + ".postHandle(" + hreq.getRequestURI() + ")");
	}

	@Override
	public void afterCompletion(HttpServletRequest hreq, HttpServletResponse hresp, Object handler, Exception ex) throws Exception {
		log().debug(() -> getClass().getSimpleName() + ".afterCompletion(" + hreq.getRequestURI() + ")");
	}
}