package vortex.application.access.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import vortex.application.ApplicationService;
import vortex.application.menu.service.MenuMapper;
import vortex.support.data.DataObject;

@Service("actionService")
public class ActionServiceImpl extends ApplicationService implements ActionService {
	private static List<String>
		permitAll,
		actions;
	private static Map<String, List<String>> actionsByPrefix;
	private static Boolean checkAccessPermission;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private MenuMapper menuMapper;
	@Resource(name="requestHandlers")
	private RequestMappingHandlerMapping requestHandlers;

	private List<String> actions() {
		if (actions == null) {
			actions = new ArrayList<>();
			Map<RequestMappingInfo, HandlerMethod> methods = requestHandlers.getHandlerMethods();
			for (RequestMappingInfo info: methods.keySet()) {
				info.getPatternsCondition().getPatterns().forEach(action -> {
					if (!actions.contains(action))
						actions.add(action);
						
				});
			}
			Collections.sort(actions);
		}
		return actions;
	}

	private Map<String, List<String>> groupedActions() {
		if (actionsByPrefix == null) {
			actionsByPrefix = DataObject.groupBy(actions(), action -> {
				int pos = action.indexOf('/', 1);
				return pos < 0 ?
					action :
					action.substring(0, pos);
			});
		}
		return actionsByPrefix;
	}

	@Override
	public List<String> getPrefixes() {
		ArrayList<String> result = new ArrayList<>(groupedActions().keySet());
		Collections.sort(result);
		return result;
	}

	@Override
	public List<String> getActions(String prefix) {
		List<String> result = groupedActions().get(prefix);
		return result != null ? result : Collections.emptyList();
	}
	
	@Override
	public int updateAction(String oldPath, String newPath) {
		return permissionMapper.updateAction(oldPath, newPath)
			 + menuMapper.updateAction(oldPath, newPath);
	}

	@Override
	public Permission.Status getPermission(String userID, String actionPath) {
		if (checkAccessPermission == null)
			checkAccessPermission = "enable".equalsIgnoreCase(properties.getString("accessPermission"));
		if (!checkAccessPermission)
			return Permission.Status.GRANTED;

		if (permitAll == null)	
			permitAll = Arrays.asList(properties.getStringArray("permitAll"));
		if (permitAll.contains(actionPath))
			return Permission.Status.GRANTED;

		if (!actions().contains(actionPath))
			return Permission.Status.ACTION_NOT_FOUND;

		log().debug(() -> "Getting permission for " + userID + " to " + actionPath);
		return permissionMapper.isPermitted(userID, actionPath) ?
			Permission.Status.GRANTED :
			Permission.Status.DENIED;
	}
}