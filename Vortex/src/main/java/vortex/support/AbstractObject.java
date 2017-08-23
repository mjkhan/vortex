package vortex.support;

import java.util.function.Supplier;

/**java.lang.Object를 확장하여 많이 쓰는 소소한 메소드들을 추가한 클래스
 * @author mjkhan
 */
public abstract class AbstractObject {
	/**lv와 rv가 같은 지 반환한다.
	 * @param lv 좌측 값
	 * @param rv 우측 값
	 * @return
	 * <ul><li>lv와 rv가 같으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	protected static final boolean equals(Object lv, Object rv) {
		return lv == rv ? true : lv != null && lv.equals(rv);
	}
	/**{@link Assert#isEmpty(Object) Assert.isEmpty(...)} 참고
	 */
	protected static final boolean isEmpty(Object obj) {
		return Assert.isEmpty(obj);
	}
	/**{@link Assert#ifEmpty(Object, Supplier) Assert.ifEmpty(...)} 참고*/
	protected static final <T> T ifEmpty(T t, Supplier<T> nt) {
		return Assert.ifEmpty(t, nt);
	}
	/**{@link Assert#ifEmpty(Object, Object) Assert.ifEmpty(...)} 참고.
	 */
	protected static final <T> T ifEmpty(T t, T nt) {
		return Assert.ifEmpty(t, nt);
	}
	/**{@link Assert#notEmpty(Object, String) Assert.notEmpty(...)} 참고.
	 */
	protected static final <T> T notEmpty(T t, String name) {
		return Assert.notEmpty(t, name);
	}
	/**{@link Assert#rootCause(Throwable) Assert.rootCause(...)} 참고
	 */
	protected static final Throwable rootCause(Throwable t) {
		return Assert.rootCause(t);
	}
	/**{@link Assert#runtimeException(Throwable) Assert.runtimeException(...)} 참고
	 */
	protected static final RuntimeException runtimeException(Throwable t) {
		return Assert.runtimeException(t);
	}
	/**현재 클래스와 연계된 Log를 반환한다.
	 * @return Log
	 */
	protected Log log() {
		return Log.get(getClass());
	}
}