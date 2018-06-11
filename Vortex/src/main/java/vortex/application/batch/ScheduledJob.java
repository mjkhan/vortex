package vortex.application.batch;

import org.springframework.stereotype.Component;

import vortex.support.Log;

@Component("scheduledJob")
public class ScheduledJob {
	public void execute() {
		Log.get(getClass()).info(() -> "Executing the scheduled job...");
	}
}