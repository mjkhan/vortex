package vortex.application;

import java.sql.Date;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import vortex.support.data.Status;

public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String
		id,
		name,
		alias,
		password,
		status;
	private Date
		createdAt,
		lastModified;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public Status status() {
		return Status.codeOf(status);
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return String.format("%s('%s', '%s')", getClass().getSimpleName(), id, name);
	}
	
	public static class Client {
		public static final Client UNKNOWN = new Client().seal();
		
		private boolean
			sealed,
			persistent;
		private String
			id,
			password,
			sessionID;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			notSealed().id = id;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			notSealed().password = password;
		}

		public String getSessionID() {
			return sessionID;
		}

		public void setSessionID(String sessionID) {
			notSealed().sessionID = sessionID;
		}
		
		public boolean isPersistent() {
			return persistent;
		}
		
		public void setPersistent(boolean persistent) {
			notSealed().persistent = persistent;
		}
		
		private Client seal() {
			sealed = true;
			return this;
		}
		
		private Client notSealed() {
			if (sealed)
				throw new RuntimeException("The client is sealed.");
			return this;
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return getId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}