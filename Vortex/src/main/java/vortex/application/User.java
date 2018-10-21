package vortex.application;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import vortex.application.group.Group;
import vortex.support.data.Status;

public class User implements UserDetails {
	public static User get(Authentication authentication) {
		Object obj = authentication != null ? authentication.getPrincipal() : null;
		return obj instanceof User ? (User)obj : User.unknown;
	}

	private static SecurityContext securityContext() {
		return SecurityContextHolder.getContext();
	}
	
	
	public static User current() {
		return get(securityContext().getAuthentication());
	}
	
	public void update() {
		securityContext().setAuthentication(
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
		createdBy,
		modifiedBy,
		status;
	private int
		failedLogin,
		passwordExpiryDays;
	private Date
		createdAt,
		passwordModified,
		lastLogin,
		lastModified;
	private List<Group> roles;
	private List<? extends GrantedAuthority> permissions;
	private List<String> permissionIDs;
	private boolean sealed;
	
	public boolean isUnknown() {
		return UNKNOWN.equals(getId());
	}
	
	public boolean isAuthenticated() {
		return !isUnknown();
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
	
	public int getFailedLogin() {
		return failedLogin;
	}
	
	public void setFailedLogin(int count) {
		failedLogin = count;
	}
	
	public Date getPasswordModified() {
		return passwordModified;
	}
	
	public void setPassworModified(Date date) {
		passwordModified = date;
	}
	
	public int getPasswordExpiryDays() {
		return passwordExpiryDays;
	}
	
	public void setPasswordExpiryDays(int days) {
		passwordExpiryDays = days;
	}
	
	public Date getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(Date date) {
		lastLogin = date;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		notSealed().createdAt = createdAt;
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
	
	public String getRoleNames() {
		return roles == null ? "" :
			roles.stream().map(role -> role.getName()).collect(Collectors.joining(", "));
	}
	
	public void setRoles(List<Group> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permissions != null ? permissions : Collections.emptyList();
	}

	public List<String> getPermissionIDs() {
		if (permissionIDs == null) {
			permissionIDs = new ArrayList<>();
			permissionIDs.add("all");
			if (!isUnknown()) {
				permissionIDs.add("authenticated");
				getAuthorities().forEach(auth -> permissionIDs.add(auth.getAuthority()));;
			}
		}
		return permissionIDs;
	}
	
	public boolean isGranted(String permissionID) {
		return getPermissionIDs().contains(permissionID);
	}
	
	public void setAuthorities(List<? extends GrantedAuthority> authorities) {
		notSealed().permissions = authorities;
		permissionIDs = null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return !status().equals(Status.LOCKED);
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return passwordExpiryDays >= 0;
	}

	@Override
	public boolean isEnabled() {
		return Status.ACTIVE.equals(status());
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
	
	@Override
	public String toString() {
		return String.format("%s('%s', '%s')", getClass().getSimpleName(), id, name);
	}
	
	public static class Password {
		public static enum Result {
			VALID("사용할 수 있는 비밀번호 입니다."),
			INVALID_LENGTH("허용되는 길이의 문자열이 아닙니다."),
			CONTAINS_USER_ID("사용자 아이디를 포함할 수 없습니다."),
			INVALID_CHARS("영문, 숫자, 특수문자를 각각  하나 이상 포함하고 있어야 합니다."),
			REPEATING_CHARS("반복되는 문자를 포함할 수 없습니다."),
			SEQUENTIAL_CHARS("연속되는 문자나 숫자를 포함할 수 없습니다.");
			
			private final String msg;

			private Result(String msg) {
				this.msg = msg;
			}
			
			public String message() {
				return msg;
			}
		}

		private static final Pattern
			alphaNumericSpecialChars = Pattern.compile("^(?=.*[~`!@#$%\\^&*()-])(?=.*\\d)(?=.*\\w).{8,16}$", Pattern.CASE_INSENSITIVE),
			repeatingChars = Pattern.compile("(.)\\1{2,}"),
			sequentialChars = Pattern.compile("\\b(\\d{3,}[^\\w]*)", Pattern.CASE_INSENSITIVE);
		
		static Result isValid(String userID, String password) {
			if (password.length() < 8)
				return Result.INVALID_LENGTH;
			if (password.contains(userID))
				return Result.CONTAINS_USER_ID;
			if (!alphaNumericSpecialChars.matcher(password).matches())
				return Result.INVALID_CHARS;
			if (repeatingChars.matcher(password).find())
				return Result.REPEATING_CHARS;
			if (sequentialChars.matcher(password).find())
				return Result.SEQUENTIAL_CHARS;
			return Result.VALID;
		}
		
		static void validate(String userID, String password) {
			Result result = isValid(userID, password);
			if (!Result.VALID.equals(result))
				throw new RuntimeException(result.message());
		}
	}
}