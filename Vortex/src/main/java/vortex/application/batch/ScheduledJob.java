package vortex.application.batch;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import vortex.application.user.service.UserService;
import vortex.support.Log;

@Component("scheduledJob")
public class ScheduledJob {
	@Resource(name="userService")
	private UserService userService;

	public void execute() {
		Log.get(getClass()).info(() -> "Executing the scheduled job...");
		userService.getUser("user00");
	}
}