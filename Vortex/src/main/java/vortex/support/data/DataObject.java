package vortex.support.data;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import vortex.support.Assert;

/**<p>기본 데이터 오브젝트.
 * </p>
 * <p>필드값에 접근할 때 사용하는 필드이름은 대소문자를 구분하지 않는다.
 * </p>
 * @author mjkhan
 */
public class DataObject extends GenericMap<Object> {
	private static final long serialVersionUID = 1L;
	/**DataObject를 생성한다.
	 */
	public DataObject() {
		caseSensitiveKey(false);
	}
	/**lv와 rv가 같은지 반환한다.
	 * @param lv 비교항의 좌측값
	 * @param rv 비교항의 우측값
	 * @return
	 * <ul><li>lv와 rv가 같으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	protected static boolean equals(Object lv, Object rv) {
		return lv == rv ? true : lv != null && lv.equals(rv);
	}
	/**{@link Assert#isEmpty(Object) Assert.isEmpty(...)} 참고.
	 */
	protected static boolean isEmpty(Object obj) {
		return Assert.isEmpty(obj);
	}
	/**{@link Assert#ifEmpty(Object, Supplier) Assert.ifEmpty(...)} 참고*/
	protected static <T> T ifEmpty(T t, Supplier<T> nt) {
		return Assert.ifEmpty(t, nt);
	}
	/**{@link Assert#notEmpty(Object, String) Assert.notEmpty(...)} 참고.
	 */
	protected static <T> T notEmpty(T t, String name) {
		return Assert.notEmpty(t, name);
	}
	/**지정하는 이름의 값들이 빈 값인지 확인한다.
	 * @param names 비어있는지 확인할 값들의 이름
	 * @return DataObject 자신
	 */
	public DataObject notEmpty(String... names) {
		for (String name: names)
			notEmpty(get(name), name);
		return this;
	}
	@Override
	public DataObject set(String key, Object value) {
		super.set(key, value);
		return this;
	}
	/**지정하는 이름의 값을 String으로 캐스팅하여 반환한다.
	 * @param name 필드 이름
	 * @return String으로 캐스팅한 값
	 * @throws ClassCastException
	 */
	public String string(String name) {
		return String.class.cast(get(name));
	}
	/**지정하는 이름의 값을 Number로 캐스팅하여 반환한다.
	 * @param name 필드 이름
	 * @return
	 * <ul><li>Number로 캐스팅한 값</li>
	 * 	   <li>값이 비어있을 경우 Integer(0)</li>
	 * </ul>
	 * @throws ClassCastException
	 */
	public Number number(String name) {
		Object obj = get(name);
		return obj != null ?  Number.class.cast(obj) : Integer.valueOf(0);
	}
	/**지정한 이름의 필드값을 boolean값으로 변환하여 반환한다.
	 * @param name 필드 이름
	 * @return
	 * <ul><li>boolean 변환한 값</li>
	 * 	   <li>빈 값이면 false</li>
	 * </ul>
	 */
	public boolean bool(String name) {
		Object obj = get(name);
		if (obj == null)
			return false;

		Boolean bool = obj instanceof Boolean ? Boolean.class.cast(obj) : Boolean.valueOf(obj.toString());
		return bool.booleanValue();
	}
	/**지정하는 이름의 필드값을 배열로 캐스팅하여 반환한다.
	 * @param name 필드이름
	 * @return 배열로 캐스팅한 필드값
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] arrays(String name) {
		return (T[])get(name);
	}
	/**지정하는 이름의 필드값을 List로 캐스팅하여 반환한다.
	 * @param name 필드이름
	 * @return List로 캐스팅한 필드값
	 */
	@SuppressWarnings("unchecked")
	public <E, T extends List<E>> T asList(String name) {
		return (T)get(name);
	}
	/**지정하는 이름의 필드값을 Map으로 캐스팅하여 반환한다.
	 * @param name 필드이름
	 * @return Map으로 캐스팅한 필드값
	 */
	@SuppressWarnings("unchecked")
	public <K, V, T extends Map<K, V>> T asMap(String name) {
		return (T)get(name);
	}
}