package vortex.application;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import vortex.support.AbstractComponent;

@Component
public class ApplicationListener extends AbstractComponent {
	private static enum Status {
		STARTED,
		STOPPED
	}
	
	private Status status;
	
	@EventListener
	public void handle(ContextRefreshedEvent evt) {
		if (Status.STARTED.equals(status)) return;
		
		status = Status.STARTED;
		log().debug(() -> "Vortex started.");
	}
	
	@EventListener
	public void handle(ContextClosedEvent evt) {
		if (Status.STOPPED.equals(status)) return;
		
		status = Status.STOPPED;
		log().debug(() -> "Vortex stopped.");
	}
}