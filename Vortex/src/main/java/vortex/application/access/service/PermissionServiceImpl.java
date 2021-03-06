package vortex.application.access.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import vortex.application.ApplicationService;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Service("permissionService")
public class PermissionServiceImpl extends ApplicationService implements PermissionService {
	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public BoundedList<DataObject> search(DataObject req) {
		return permissionMapper.search(req);
	}

	@Override
	public DataObject getInfo(String permissionID) {
		return permissionMapper.getInfo(permissionID);
	}

	@Override
	public Permission getPermission(String permissionID) {
		return permissionMapper.getPermission(permissionID);
	}

	@Override
	public boolean create(Permission permission) {
		return permissionMapper.create(permission);
	}

	@Override
	public boolean update(Permission permission) {
		return permissionMapper.update(permission);
	}
	
	@Override
	public BoundedList<DataObject> getActions(DataObject req) {
		return permissionMapper.getActions(req);
	}

	@CacheEvict(value="menuContext", allEntries=true)
	@Override
	public int addActions(String permissionID, String... actionPaths) {
		return permissionMapper.addActions(permissionID, actionPaths);
	}

	@CacheEvict(value="menuContext", allEntries=true)
	@Override
	public int deleteActions(String permissionID, String... actionPaths) {
		return permissionMapper.deleteActions(permissionID, actionPaths);
	}

	@CacheEvict(value="menuContext", allEntries=true)
	@Override
	public int delete(String... permissionIDs) {
		return permissionMapper.delete(permissionIDs);
	}
}