package vortex.support.data.hierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**<p>객체의 계층구조를 생성한다. 이 때 객체는 HierarchyElement나 CompositeElement를 구현해야 한다.</p>
 * <p>{@link #setElements(Collection) 대상요소들을 설정}한 후 {@link #build() build()} 메소드를 실행하면,<br />
 * 계층구조의  {@link #get() 여러 최상위 요소들}이나 {@link #get(Object) 루트요소}를 얻을 수 있다.</p>
 * <p>다음은 HierarchyBuilder로 객체의 계층 구조를 생성하는 예이다.
 * <pre class="shade"><code> Collection&lt;MyHierarchyElement> elements = ...;
 * //최상위 요소의 ID를 알 경우
 * CompositeElement top = new HierarchyBuilder&lt;MyHierarchyElement>().set(elements).get("00000");
 * //최상위 요소의 ID를 모르거나 최상위 요소들의 리스트를 얻으려 할 경우
 * Collection&lt;MyHierarchyElement> tops = new HierarchyBuilder&lt;MyHierarchyElement>().set(elements).get();
 * //계층구조 요소들의 Hierarchy를 얻으려 할 경우
 * Hierarchy&lt;MyHierarchyElement> hierarchy = new HierarchyBuilder&lt;MyHierarchyElement>().set(elements).build();</code></pre>
 * </p>
 * <p>HierarchyBuilder는 다음 중 한 조건을 충족하면 최상위 요소로 간주한다.
 * <ul><li>{@link #testTop(Predicate) HierarchyBuilder에 설정한 조건식}이 계층요소의 HierarchyInfo를 분석하여 최상위 요소로 판단할 경우</li>
 *     <li>계층요소가 반환하는 HierarchyInfo가 해당 요소객체를 <a href="HierarchyElement.HierarchyInfo.html#top-element">최상위 요소</a>로 명시할 경우</li>
 * 	   <li>설정된 요소객체 중에서 부모 객체를 찾지 못했을 경우</li>
 * </ul></p>
 * @param <T> HierarchyElement 타입
 */
public class HierarchyBuilder<T extends HierarchyElement> {
	private HashMap<String, T> index;
	private Collection<T> elements;
	private Predicate<HierarchyElement.HierarchyInfo> atTop;
	/**계층구조로 생성할 요소객체들을 설정한다.
	 * @param elements 계층구조로 생성할 요소객체들
	 * @return HierarchyBuilder
	 */
	public HierarchyBuilder<T> setElements(Collection<T> elements) {
		if (elements == null)
			throw new IllegalArgumentException("elements: null");
		this.elements = elements;
		createIndex();
		return this;
	}
	/**주어진 HierarchyElement가 최상위 요소인지를 판단하는 조건식을 설정한다.
	 * @param test 주어진 HierarchyElement가 최상위 요소인지를 판단하는 조건식
	 * @return HierarchyBuilder
	 */
	public HierarchyBuilder<T> testTop(Predicate<HierarchyElement.HierarchyInfo> test) {
		atTop = test;
		return this;
	}

	private void createIndex() {
		index = new HashMap<String, T>();
		for (T e: elements)
			index.put(e.hierarchyInfo().ID(), e);
	}
	/**계층구조를 생성하는데 사용된 HierarchyElement들의 색인을 반환한다.
	 * @return 계층구조를 생성하는데 사용된 HierarchyElement들의 색인
	 */
	public Map<String, T> getIndex() {
		return index;
	}

	private void add(T lv, T rv) {
		if (lv == null || rv == null || lv == rv) return;

		CompositeElement parent = (CompositeElement)lv;
		parent.add(rv);
	}
	/**계층구조를 생성하고 주어진 ID의 루트 객체를 반환한다.
	 * @param rootID 루트 객체의 ID. 주어진 ID의 객체는 설정된 요소 객체들 중에 존재해야 한다.
	 * @return 생성된 계층구조의 루트 요소
	 */
	public CompositeElement get(Object rootID) {
		if (elements == null) return null;

		T e = index.get(rootID);
		if (e == null)
			throw new RuntimeException("Root element not found: " + rootID);
		if (!(e instanceof CompositeElement))
			throw new RuntimeException("Root not of CompositeElement: " + rootID);

		CompositeElement root = CompositeElement.class.cast(e);
		for (T element: elements) {
			if (element == null) continue;
			Object parentID = element.hierarchyInfo().parentID();
			T parent = parentID == null ? null : index.get(parentID);
			add(parent, element);
		}
		return root;
	}
	/**계층구조를 생성하고 최상위 요소들을 반환한다.
	 * @return 계층구조의 최상위 요소들
	 */
	public Collection<T> get() {
		if (elements == null) return null;

		ArrayList<T> tops = new ArrayList<T>();
		for (T e: elements) {
			boolean top = atTop != null ? atTop.test(e.hierarchyInfo()) : e.hierarchyInfo().atTop();
			if (top) {
				if (!tops.contains(e))
					tops.add(e);
			} else {
				T parent = index.get(e.hierarchyInfo().parentID());
				if (parent == null) {
					if (!tops.contains(e))
						tops.add(e);
				} else {
					add(parent, e);
				}
			}
		}
		return tops;
	}
	/**계층구조를 생성하고 Hierarchy를 반환한다.<br />
	 * 이 때 요소객체들은 {@link #get() get()} 메소드를 실행하여 생성된다.
	 * @return Hierarchy
	 */
	public Hierarchy<T> build() {
		return build(Hierarchy::new);
	}
	/**Hierarchy를 생성하고 반환한다.<br />
	 * 요소 객체들은 {@link #get() get()} 메소드를 실행하여 얻어진다.
	 * Hierarchy는 주어진 factory로 생성된다.
	 * @param factory Hierarchy의 supplier
	 * @return Hierarchy
	 */
	public <H extends Hierarchy<T>> H build(Supplier<H> factory) {
		Collection<T> tops = get();
		H h = factory.get();
		h.setElements(elements).setIndex(index).setTops(tops);
		return h;
	}
	/**계층구조를 생성하는데 쓰인 내부자원을 비우고 정리한다.
	 */
	public void clear() {
		if (index != null) {
			index.clear();
			index = null;
		}
		elements = null;
	}
}