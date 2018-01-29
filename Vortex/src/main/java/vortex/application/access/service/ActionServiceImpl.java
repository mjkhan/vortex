package vortex.application.access.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import vortex.application.ApplicationService;
import vortex.application.group.Group;
import vortex.application.group.GroupMapper;
import vortex.support.data.DataObject;

@Service("actionService")
public class ActionServiceImpl extends ApplicationService implements ActionService {
	@Resource(name="actionGroup")
	private GroupMapper actionGroup;
	@Autowired
	private ActionMapper actionMapper;
	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public List<DataObject> getGroups(DataObject req) {
		return actionGroup.search(req);
	}

	@Override
	public DataObject getGroupInfo(String groupID) {
		return actionGroup.getInfo(groupID);
	}

	@Override
	public Group getGroup(String groupID) {
		return actionGroup.getGroup(groupID);
	}

	@Override
	public boolean create(Group group) {
		return actionGroup.create(group);
	}

	@Override
	public boolean update(Group group) {
		return actionGroup.update(group);
	}

	@Override
	public int deleteGroups(String... groupIDs) {
		return
			permissionMapper.delete(groupIDs, null)
		  + actionMapper.delete(groupIDs, null)
		  + actionGroup.deleteGroups(groupIDs);
	}

	@Override
	public List<DataObject> getActions(String groupID) {
		return actionMapper.getActions(groupID);
	}

	@Override
	public DataObject getInfo(String actionID) {
		return actionMapper.getInfo(actionID);
	}

	@Override
	public Action getAction(String actionID) {
		return actionMapper.getAction(actionID);
	}

	@Override
	public boolean create(Action action) {
		return actionMapper.create(action);
	}

	@Override
	public boolean update(Action action) {
		return actionMapper.update(action);
	}

	@Override
	public int delete(String... actionIDs) {
		return permissionMapper.deleteActions(null, actionIDs)
			 + actionMapper.delete((String[])null, actionIDs);
	}
	
	private static List<String> permitAll;
	private static final ArrayList<String> actions = new ArrayList<>();
	private static Boolean checkAccessPermission;
	@Resource(name="requestHandlers")
	private RequestMappingHandlerMapping requestHandlers;
	
	private ArrayList<String> actions() {
		if (actions.isEmpty()) {
			Map<RequestMappingInfo, HandlerMethod> methods = requestHandlers.getHandlerMethods();
			for (RequestMappingInfo info: methods.keySet()) {
				actions.addAll(info.getPatternsCondition().getPatterns().stream().distinct().collect(Collectors.toList()));
			}
			Collections.sort(actions);
		}
		return actions;
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