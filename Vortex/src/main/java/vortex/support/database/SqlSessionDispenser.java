package vortex.support.database;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;

public class SqlSessionDispenser {
	private static final ThreadLocal<HashMap<String, SqlSession>> cache = new ThreadLocal<HashMap<String, SqlSession>>() {
		@Override
		protected java.util.HashMap<String, SqlSession> initialValue() {
			return new HashMap<String, SqlSession>();
		};
	};
	
	public static SqlSession get(String name) {
		return cache.get().get(name);
	}
	
	public static void set(String name, SqlSession sqlSession) {
		cache.get().put(name, sqlSession);
	}
	
	public static void remove(String name) {
		cache.get().remove(name);
	}
}