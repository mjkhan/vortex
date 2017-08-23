package vortex.support.data;

import java.util.LinkedHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**String 키와  T값의 Map으로 편의성 메소드들을 추가하였다.<br />
 * {@link #caseSensitiveKey(boolean) 키의 대소문자 구분여부}를 지정할 수 있다.<br />
 * 디폴트는 대소문자를 구분하도록 되어있다..
 * @author mjkhan
 * @param <T> 값의 타입
 */
public class GenericMap<T> extends LinkedHashMap<String, T> {
	private static final long serialVersionUID = 1L;

	private boolean caseSensitiveKey = true;
	/**키의 대소문자 구분여부를 반환한다.
	 * @return
	 * <ul><li>대소문자를 구분하면 true</li>
	 * 	   <li>대소문자를 구분하지 않으면 false</li>
	 * </ul>
	 */
	public boolean caseSensitiveKey() {
		return caseSensitiveKey;
	}
	/**키의 대소문자 구분여부를 설정한다.
	 * @param sensitive
	 * <ul><li>대소문자를 구분하려면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 * @return GenericMap 자신
	 */
	public GenericMap<T> caseSensitiveKey(boolean sensitive) {
		caseSensitiveKey = sensitive;
		return this;
	}

	private String findKey(Object obj) {
		String s = (String)obj;
		if (caseSensitiveKey)
			return s;
		for (String key: keySet())
			if (key.equalsIgnoreCase(s))
				return key;
		return s;
	}
	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(key) ? true : super.containsKey(findKey(key));
	}
	@Override
	public T get(Object key) {
		T t = super.get(key);
		return t != null ? t : super.get(findKey(key));
	}
	@Override
	public T getOrDefault(Object key, T defaultValue) {
		return super.getOrDefault(findKey(key), defaultValue);
	}
	@Override
	public T put(String key, T value) {
		return super.put(findKey(key), value);
	}
	/**value를 지정하는 key의 값으로 설정한다.
	 * @param key	키
	 * @param value 키로 연결할 값
	 * @return GenericMap 자신
	 */
	public GenericMap<T> set(String key, T value) {
		put(key, value);
		return this;
	}
	@Override
	public T remove(Object key) {
		T t = super.remove(key);
		return t != null ? t : super.remove(findKey(key));
	}
	@Override
	public T computeIfAbsent(String key, Function<? super String, ? extends T> mappingFunction) {
		return super.computeIfAbsent(findKey(key), mappingFunction);
	}
	@Override
	public T computeIfPresent(String key, BiFunction<? super String, ? super T, ? extends T> remappingFunction) {
		return super.computeIfPresent(findKey(key), remappingFunction);
	}
	@Override
	public T merge(String key, T value, BiFunction<? super T, ? super T, ? extends T> remappingFunction) {
		return super.merge(findKey(key), value, remappingFunction);
	}
	@Override
	public T putIfAbsent(String key, T value) {
		return super.putIfAbsent(findKey(key), value);
	}
}