package vortex.application.access.service;

import java.util.List;

public interface ActionService {
	public List<String> getPrefixes();
	
	public List<String> getActions(String prefix);
	
	public int changeAction(String oldName, String newName);
	
	public Permission.Status getPermission(String userID, String actionPath);
}