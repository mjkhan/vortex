package vortex.application;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import vortex.support.AbstractObject;
import vortex.support.data.DataObject;

public class VortexTest extends AbstractObject {
	protected ClassPathXmlApplicationContext actx = new ClassPathXmlApplicationContext("classpath:/**/*.xml");
	
	@SuppressWarnings("unchecked")
	protected <T> T getBean(String name) {
		return (T)actx.getBean(name);
	}
	
	protected DataObject dataObject() {
		return new DataObject();
	}
}