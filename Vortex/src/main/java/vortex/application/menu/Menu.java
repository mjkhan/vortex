package vortex.application.menu;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import vortex.support.data.Status;
import vortex.support.data.hierarchy.CompositeElement;
import vortex.support.data.hierarchy.HierarchyElement;

public class Menu implements CompositeElement {
	public static final String ROOT_ID = "00000";
	
	private String
		id,
		name,
		actionID,
		actionPath,
		imageConfig,
		parentID,
		modifiedBy,
		status;
	private int sortOrder;
	private Date lastModified;

	private Menu parent;
	private ArrayList<Menu> children;
	private HierarchyInfo hierarchyInfo;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Menu getParent() {
		return parent;
	}
	
	public String getParentID() {
		return parentID != null ? parentID : ROOT_ID;
	}
	
	public void setParentID(String parentId) {
		this.parentID = parentId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getLevel() {
		return parent != null ? parent.getLevel() + 1 : 0;
	}
	
	public String getActionID() {
		return actionID;
	}
	
	public void setActionID(String actionID) {
		this.actionID = actionID;
	}
	
	public String getAction() {
		if (!isActive()) return null;
		
		if (actionPath != null)
			return actionPath;
		
		Menu child = activeChild();
		return child != null ? child.getAction() : null;
	}
	
	public String getActionPath() {
		return actionPath;
	}
	
	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}
	
	public String getImageConfig() {
		return imageConfig;
	}
	
	public void setImageConfig(String imageConfig) {
		this.imageConfig = imageConfig;
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}
	
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public int getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public Status status() {
		return Status.codeOf(getStatus());
	}
	
	public boolean isActive() {
		return Status.ACTIVE.equals(status());
	}
	
	public String getStatus() {
		return status != null ? status : Status.ACTIVE.code();
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public HierarchyInfo hierarchyInfo() {
		if (hierarchyInfo == null)
			hierarchyInfo = new HierarchyInfo();
		return hierarchyInfo.setID(id).setParentID(parentID);
	}

	@Override
	public void add(HierarchyElement e) {
		if (!(e instanceof Menu) || equals(e)) return;
		
		if (children == null)
			children = new ArrayList<Menu>(); 
		Menu child = (Menu)e;
		if (children.contains(child)) return;
		
		children.add(child);
		child.parent = this;
	}

	@Override
	public List<Menu> getChildren() {
		return children != null ? children : Collections.emptyList();
	}
	
	private Menu activeChild() {
		for (Menu child: getChildren())
			if (child.isActive())
				return child;
		return null;
	}
	
	public Menu getPermitted(Map<String, List<String>> actionPermissions) {
		if (!isActive()) return null;
		if (actionPath != null) {
			List<String> permissions = actionPermissions.get(actionPath);
			return permissions != null && !permissions.isEmpty() ? this : null;
		}
		List<Menu> children = getChildren();
		if (children.isEmpty()) return null;
		
		for (Menu child: children) {
			Menu permitted = child.getPermitted(actionPermissions);
			if (permitted != null)
				return permitted;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("%s('%s', '%s')", getClass().getSimpleName(), id, name);
	}
}