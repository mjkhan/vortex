package vortex.support.data;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**String 키와  T값의 Map으로 편의성 메소드들을 추가하였다.<br />
 * {@link #caseSensitiveKey(boolean) 키의 대소문자 구분여부}를 지정할 수 있다.<br />
 * 디폴트는 대소문자를 구분하도록 되어있다..
 * @author mjkhan
 * @param <E> 값의 타입
 */
public class GenericMap<E> extends LinkedHashMap<String, E> {
	/**objs들을 groupMapper가 반환하는 값을 기준으로 grouping하여 반환한다.
	 * @param objs			객체 목록
	 * @param groupMapper	요소 중 키로 쓰일 값을 반환
	 * @return groupMapper가 반환하는 값을 기준으로 grouping하여 만든 결과 Map
	 */
	public static <E> Map<String, List<E>> groupBy(Collection<E> objs, Function<E, String> groupMapper) {
		return objs != null ?
			objs.stream().collect(Collectors.groupingBy(groupMapper)) :
			Collections.emptyMap();
	}

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
	public <T extends GenericMap<E>> T caseSensitiveKey(boolean sensitive) {
		caseSensitiveKey = sensitive;
		return (T)this;
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
	public E get(Object key) {
		E t = super.get(key);
		return t != null ? t : super.get(findKey(key));
	}
	@Override
	public E getOrDefault(Object key, E defaultValue) {
		return super.getOrDefault(findKey(key), defaultValue);
	}
	@Override
	public E put(String key, E value) {
		return super.put(findKey(key), value);
	}
	/**value를 지정하는 key의 값으로 설정한다.
	 * @param key	키
	 * @param value 키로 연결할 값
	 * @return GenericMap 자신
	 */
	public <T extends GenericMap<E>> T set(String key, E value) {
		put(key, value);
		return (T)this;
	}
	@Override
	public E remove(Object key) {
		E t = super.remove(key);
		return t != null ? t : super.remove(findKey(key));
	}
	@Override
	public E computeIfAbsent(String key, Function<? super String, ? extends E> mappingFunction) {
		return super.computeIfAbsent(findKey(key), mappingFunction);
	}
	@Override
	public E computeIfPresent(String key, BiFunction<? super String, ? super E, ? extends E> remappingFunction) {
		return super.computeIfPresent(findKey(key), remappingFunction);
	}
	@Override
	public E merge(String key, E value, BiFunction<? super E, ? super E, ? extends E> remappingFunction) {
		return super.merge(findKey(key), value, remappingFunction);
	}
	@Override
	public E putIfAbsent(String key, E value) {
		return super.putIfAbsent(findKey(key), value);
	}

	/**지정하는 key에 설정된 값이 없으면 value를 설정한다.
	 * @param key	키
	 * @param value	설정할 값
	 * @return GenericMap 자신
	 */
	public <T extends GenericMap<E>> T setIfAbsent(String key, E value) {
		putIfAbsent(key, value);
		return (T)this;
	}
}