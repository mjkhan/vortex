package vortex.application.user.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;

import vortex.application.DataMapper;
import vortex.application.User;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

@Repository("userMapper")
public class UserMapper extends DataMapper {
	
	public BoundedList<DataObject> search(DataObject params) {
		if (isEmpty(params.get("searchTerms"))) {
			params.remove("searchBy");
			params.remove("searchTerms");
		}
		
		return boundedList(selectList("user.search", params), params);
	}
	
	public List<DataObject> getInfo(String... userIDs) {
		return selectList("user.getInfo", params().set("userIDs", userIDs));
	}
	
	public List<User> getUsers(String... userIDs) {
		return selectList("user.getUsers", params().set("userIDs", userIDs));
	}
	
	public User getUser(String userID) {
		List<User> users = getUsers(userID);
		return !users.isEmpty() ? users.get(0) : null;
	}
	
	public boolean create(User user) {
		if (user == null) return false;
		
		User currentUser = currentUser();
		if (currentUser.isUnknown())
			currentUser = user;
		return insert("user.insert",
			params()
				.set("user", user)
				.set("currentUser", currentUser)
		) == 1;
	}
	
	public boolean update(User user) {
		return user == null ? false :
			update("user.update", params(true).set("user", user)) == 1;
	}
	
	public int setStatus(String status, String... userIDs) {
		return isEmpty(status) ? 0 :
		update(
			"user.setStatus",
			params(true)
				.set("status", status)
				.set("userIDs", userIDs)
		);
	}
	
	public int remove(String... userIDs) {
		return setStatus(Status.REMOVED.code(), userIDs);
	}
	
	public int deleteUsers(String... userIDs) {
		return delete(
			"user.delete",
			params().set("userIDs", userIDs)
		);
	}
	
	static class Password {
		static enum Result {
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