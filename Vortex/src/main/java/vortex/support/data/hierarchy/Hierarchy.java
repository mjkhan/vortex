package vortex.support.data.hierarchy;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import vortex.support.AbstractComponent;
/**HierarchyElement의 집합체로, {@link HierarchyBuilder HierarchyBuilder}로 생성한다.
 * Hierarchy는
 * <ul><li>{@link #getElements() 계층구조 요소}</li>
 *     <li>{@link #getIndex() 계층구조 요소들의 색인}</li>
 *     <li>{@link #topElements() 계층의 최상위 요소들}</li>
 *     <li>{@link #getTop() 계층의 루트 요소}</li>
 *     <li>{@link #get(String) ID에 해당하는 요소}</li>
 * </ul>
 * @param <T> HierarchyElement 타입
 */
public class Hierarchy<T> extends AbstractComponent implements Serializable {
	private static final long serialVersionUID = 1L;
	private Collection<T>
		elements,
		tops;
	private Map<String, T> index;
	/**Hierarchy가 비어있는지 반환한다.
	 * @return
	 * <ul><li>Hierarchy가 비어있으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean isEmpty() {
		return isEmpty(index) || isEmpty(elements);
	}
	/**계층 구성요소들의 색인을 반환한다.
	 * @return Hierarchy 구성요소들의 색인
	 */
	public Map<String, T> getIndex() {
		return ifEmpty(index, Collections::emptyMap);
	}

	Hierarchy<T> setIndex(Map<String, T> index) {
		this.index = index;
		return this;
	}
	/**계층의 구성요소들을 반환한다..
	 * @return 계층의 구성요소들
	 */
	public Collection<T> getElements() {
		return ifEmpty(elements, Collections::emptyList);
	}

	Hierarchy<T> setElements(Collection<T> elements) {
		this.elements = elements;
		return this;
	}
	/**계층의 최상위 요소들을 반환한다.
	 * @return 계층의 최상위 요소들
	 */
	public Collection<T> topElements() {
		return ifEmpty(tops, Collections::emptyList);
	}
	/**계층의 루트 요소, 또는 첫번째 최상위 요소를 반환한다.
	 * @return 계층의 루트 요소, 또는 첫번째 최상위 요소
	 */
	public T getTop() {
		return !isEmpty() ? tops.iterator().next() : null;
	}

	Hierarchy<T> setTops(Collection<T> tops) {
		this.tops = tops;
		return this;
	}
	/**주어진 ID의 HierarchyElement를 반환한다.
	 * @param 계층구조의 ID
	 * @return 주어진 ID의 HierarchyElement
	 */
	public T get(String elementID) {
		return !isEmpty() ? index.get(elementID) : null;
	}
}