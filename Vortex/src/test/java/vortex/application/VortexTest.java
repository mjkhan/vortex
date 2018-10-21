package vortex.application;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import vortex.support.AbstractObject;
import vortex.support.data.DataObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/context-*.xml"})
public class VortexTest extends AbstractObject {
	
	@SuppressWarnings("unchecked")
	protected <T> T getBean(String name) {
		return null; //(T)actx.getBean(name);
	}
	
	protected DataObject dataObject() {
		return new DataObject();
	}
}