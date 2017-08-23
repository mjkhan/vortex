package vortex.support;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**Assertion 유틸리티
 * @author mjkhan
 * */
public class Assert {
	private Assert() {}
	/**obj가 null인지, 공백문자인지, 빈 collection인지, 빈 배열인지 반환한다.
	 * @param obj 오브젝트
	 * @return
	 * <ul><li>obj가 null인지, 공백문자인지, 빈 collection인지, 빈 배열이면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public static final boolean isEmpty(Object obj) {
		if (obj == null) return true;
		if (obj instanceof String) {
			String str = (String)obj;
			return str.trim().isEmpty();
		}
		if (obj instanceof Object[]) {
			Object[] objs = (Object[])obj;
			return objs.length < 1;
		}
		if (obj instanceof Iterable) {
			Iterable<?> objs = (Iterable<?>)obj;
			return !objs.iterator().hasNext();
		}
		return false;
	}
	/**t가 {@link #isEmpty(Object) 공백이면} nt가 반환하는 값을, 그렇지 않으면 t를 반환한다.
	 * @param <T>	타입
	 * @param t		오브젝트
	 * @param nt	공백일 경우 nt를 반환할 Supplier</code>
	 * @return
	 * <ul><li>t가 공백이 아니면 t</li>
	 * 	   <li>t가 공백이면 Supplier가 반환하는 값</li>
	 * </ul>
	 */
	public static final <T> T ifEmpty(T t, Supplier<T> nt) {
		return !isEmpty(t) ? t : nt != null ? nt.get() : t;
	}
	/**t가 {@link #isEmpty(Object) 공백이면} nt를, 그렇지 않으면 t를 반환한다.
	 * @param <T>	타입
	 * @param t		오브젝트
	 * @param nt	공백일 경우 반환할 값
	 * @return
	 * <ul><li>t가 공백이 아니면 t</li>
	 * 	   <li>t가 공백이면 nt</li>
	 * </ul>
	 */
	public static final <T> T ifEmpty(T t, T nt) {
		return ifEmpty(t, () -> nt);
	}
	/**t가 공백이 아닌지 확인하고 t를 반환한다.<br />
	 * 올바로 동작하려면 JVM을 시작할 때 <code>-enableassertion:vortex.Assert</code>나 <code>-ea:vortex.Assert</code> 옵션을 지정해야 한다.
	 * @param t		오브젝트
	 * @param name	assertion 실패 시 오류 메시지에 사용할 이름
	 * @param <T>	타입
	 * @return t
	 * @throws t가 공백일 경우 NullPointerException
	 */
	public static final <T> T assertNotEmpty(T t, String name) {
		try {
			assert !isEmpty(t);
			return t;
		} catch (AssertionError e) {
			throw new NullPointerException(name + " is null or empty.");
		}
	}
	/**t가 공백이 아닌지 확인하고 t를 반환한다.<br />
	 * t가 {@link #isEmpty(Object) 공백이면}, NullPointerException을 발생시킨다.
	 * @param t		오브젝트
	 * @param name	assertion 실패 시 오류 메시지에 사용할 이름
	 * @return t
	 * @throws t가 공백일 경우NullPointerException
	 */
	public static final <T> T notEmpty(T t, String name) {
		if (!isEmpty(t)) return t;
		throw new NullPointerException(name + " is null or empty.");
	}
	/**t의 최초원인이 되는 Throwable을 반환한다.
	 * @param t Throwable
	 * @return t의 최초원인이 되는 Throwable
	 */
	public static final Throwable rootCause(Throwable t) {
		Throwable cause = t != null ? t.getCause() : null;
		if (cause == null
		 && t instanceof InvocationTargetException)
			cause = ((InvocationTargetException)t).getTargetException();
		return cause == null || cause == t ? t : rootCause(cause);
	}
	/**t의 최초 원인이 된 예외를 RuntimeException에 담아 반환한다.
	 * @param t 예외
	 * @return RuntimeException
	 */
	public static final RuntimeException runtimeException(Throwable t) {
		t = rootCause(t);
		return t instanceof RuntimeException ? RuntimeException.class.cast(t) : new RuntimeException(t);
	}
}