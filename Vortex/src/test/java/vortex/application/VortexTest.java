package vortex.application;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import vortex.support.AbstractObject;

public class VortexTest extends AbstractObject {
	protected ClassPathXmlApplicationContext actx = new ClassPathXmlApplicationContext("classpath:/**/*.xml");
	
	@SuppressWarnings("unchecked")
	protected <T> T getBean(String name) {
		return (T)actx.getBean(name);
	}
}