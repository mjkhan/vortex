package vortex.application.access.service;

import org.springframework.stereotype.Repository;

import vortex.application.EntityMapper;

@Repository("permissionMapper")
public class PermissionMapper extends EntityMapper<Permission> {
	@Override
	protected String namespace() {
		return entityName().toLowerCase();
	}

	@Override
	protected String entityName() {
		return Permission.class.getSimpleName();
	}
	
	@Override
	public int setStatus(String status, String... objIDs) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int delete(String... permissionIDs) {
		return deleteActions(permissionIDs, null)
			 + super.delete(permissionIDs);
	}
	
	public int addActions(String[] permissionIDs, String[] actionIDs) {
		if (isEmpty(permissionIDs) || isEmpty(actionIDs)) return 0;
		return insert(
			namespace() + ".addActions"
		   ,params(true)
		   	.set("permissionIDs", permissionIDs)
		   	.set("actionIDs", actionIDs)
		);
	}
	
	public int addActions(String permissionID, String... actionIDs) {
		return !isEmpty(permissionID) ?
			addActions(new String[]{permissionID}, actionIDs) :
			0;
	}
	
	public int deleteActions(String[] permissionIDs, String[] actionIDs) {
		return delete(
			namespace() + ".deleteActions"
		   ,params()
		   	.set("permissionIDs", ifEmpty(permissionIDs, () -> null))
		   	.set("actionIDs", ifEmpty(actionIDs, () -> null))
		);
	}
	
	public int deleteActions(String permissionID, String... actionIDs) {
		return deleteActions(new String[]{permissionID}, actionIDs);
	}
}