package vortex.support.data.hierarchy;

import java.util.Collection;
import java.util.function.Predicate;

import vortex.support.AbstractComponent;

/**계층구조의 객체들을 String 형태로 변환한다.
 * @author mjkhan
 * @param <T> HierarchyElement 타입
 */
public class Stringify<T extends HierarchyElement> extends AbstractComponent {
	/**HierarchyElement를 String으로 변환하는 Functional interface.
	 * @author mjkhan
	 * @param <T> HierarchyElement 타입
	 */
	@FunctionalInterface
	public static interface ToString<T extends HierarchyElement> {
		/**계층요소 객체와 해당 객체의 레벨을 사용하여 String으로 변환하여 반환한다.
		 * @param element 계층요소 객체
		 * @param level 해당객체의 레빌
		 * @return 변환된 String
		 */
		public String get(T element, int level);
	}
		
	private ToString<T>
		emptyString = (e, level) -> "",
		beginElement = emptyString,
		endElement = emptyString,
		beginChildren = emptyString,
		endChildren = emptyString;
	private Predicate<T> proceed;
	
	/**주어진 s를 n번 반복하여 들여쓰기에 사용될 문자열을 반환한다.
	 * @param s 문자열
	 * @param n 반복할 횟수
	 * @return 들여쓰기에 사용될 문자열
	 */
	public static final String indent(String s, int n) {
		String result = "";
		for (int i = 0; i < n; ++i) {
			result += s;
		}
		return result;
	}

	/**계층요소가 시작되는 지점에 문자열을 생성할 interface를 설정한다.
	 * @param begin 계층요소가 시작되는 지점에 문자열을 생성할 interface
	 * @return Stringify
	 */
	public Stringify<T> beginElement(ToString<T> begin) {
		this.beginElement = begin;
		return this;
	}

	/**계층요소가 끝나는 지점에 문자열을 생성할 interface를 설정한다.
	 * @param begin 계층요소가 끝나는 지점에 문자열을 생성할 interface
	 * @return Stringify
	 */
	public Stringify<T> endElement(ToString<T> end) {
		this.endElement = end;
		return this;
	}

	/**하위 계층요소가 시작되는 지점에 문자열을 생성할 interface를 설정한다.
	 * @param begin 하위 계층요소가 시작되는 지점에 문자열을 생성할 interface
	 * @return Stringify
	 */
	public Stringify<T> beginChildren(ToString<T> begin) {
		this.beginChildren = begin;
		return this;
	}

	/**하위 계층요소가 끝나는 지점에 문자열을 생성할 interface를 설정한다.
	 * @param begin 하위 계층요소가 끝나는 지점에 문자열을 생성할 interface
	 * @return Stringify
	 */
	public Stringify<T> endChildren(ToString<T> end) {
		this.endChildren = end;
		return this;
	}
	
	public Stringify<T> proceed(Predicate<T> proceed) {
		this.proceed = proceed;
		return this;
	}

	/**주어진 계층요소들을 문자열로 변환하여 반환한다.
	 * @param elements 문자열로 변환할 계층요소들
	 * @return 계층요소들에서 변환된 문자열
	 */
	public String toString(Iterable<T> elements) {
		if (isEmpty(elements)) return "";

		StringBuilder buff = new StringBuilder();
		convert(elements, buff, 0);
		return buff.toString().trim();
	}
	
	private void convert(Iterable<T> elements, StringBuilder buff, int level) {
		elements.forEach(element -> {
			if (element == null) return;
			
			boolean parent = element instanceof CompositeElement;
			Collection<? extends HierarchyElement> children = parent ? ((CompositeElement)element).getChildren() : null;
			parent = parent && !isEmpty(children);
			
			buff.append(beginElement.get(element, level));
			if (parent) {
				int sublevel = level + 1;
				buff.append(beginChildren.get(element, sublevel));
				convert((Iterable<T>)children, buff, sublevel);
				buff.append(endChildren.get(element, sublevel));
			}
			buff.append(endElement.get(element, level));
		});
	}
}