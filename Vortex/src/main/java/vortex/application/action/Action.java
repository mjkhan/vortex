package vortex.application.action;

import java.sql.Date;

public class Action {
	private String
		id,
		groupID,
		name,
		description,
		modifiedBy;
	private Date lastModified;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getGroupID() {
		return groupID;
	}
	
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}
	
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		return "Action{id:" + id + ", name:" + name + "}";
	}
}