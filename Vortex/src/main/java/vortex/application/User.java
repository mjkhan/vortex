package vortex.application;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import vortex.support.data.Status;

public class User implements UserDetails {
	public static User current() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			return User.unknown;
		return (User)auth.getPrincipal();
	}
	
	public void update() {
		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken(this, getPassword(), getAuthorities())
		);
	}
	
	private static final long serialVersionUID = 1L;
	private static final String UNKNOWN = "unknown";
	public static final User unknown = new User().seal();

	private String
		id,
		name,
		alias,
		password,
		status;
	private Date
		createdAt,
		lastModified;
	private List<? extends GrantedAuthority> roles;
	private List<String> roleIDs;
	private boolean sealed;
	
	public boolean isUnknown() {
		return UNKNOWN.equals(getId());
	}
	
	public List<String> getRoleIDs() {
		if (roleIDs == null) {
			roleIDs = new ArrayList<>();
			roleIDs.add("all");
			if (!isUnknown()) {
				roleIDs.add("authenticated");
				getAuthorities().forEach(auth -> roleIDs.add(auth.getAuthority()));;
			}
		}
		return roleIDs;
	}
	
	public String getId() {
		return id != null ? id : UNKNOWN;
	}
	
	public void setId(String id) {
		notSealed().id = id;
	}

	@Override
	public String getUsername() {
		return getId();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		notSealed().name = name;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		notSealed().alias = alias;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		notSealed().password = password;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		notSealed().createdAt = createdAt;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(Date lastModified) {
		notSealed().lastModified = lastModified;
	}
	
	public Status status() {
		return Status.codeOf(status);
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		notSealed().status = status;
	}
	
	@Override
	public String toString() {
		return String.format("%s('%s', '%s')", getClass().getSimpleName(), id, name);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles != null ? roles : Collections.emptyList();
	}
	
	public void setAuthorities(List<? extends GrantedAuthority> authorities) {
		notSealed().roles = authorities;
		roleIDs = null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	private User seal() {
		sealed = true;
		return this;
	}
	
	private User notSealed() {
		if (sealed)
			throw new IllegalStateException(this + " is sealed");
		return this;
	}
}