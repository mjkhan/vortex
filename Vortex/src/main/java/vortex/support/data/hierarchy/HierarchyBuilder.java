package vortex.support.data.hierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**<p>객체의 계층구조를 생성한다. 이 때 객체는 HierarchyElement나 CompositeElement를 구현해야 한다.</p>
 * <p>{@link #setElements(Collection) Setting elements} to work with,<br />
 * you can get a {@link #build() hierarchy} either of {@link #get() multiple top elements} or of a {@link #get(Object) single root element}.</p>
 * <p>Following is a typical example of a HierarchyBuilder creating a hierarchy of objects.
 * <pre class="shade"><code> Collection&lt;MyHierarchyElement> elements = ...;
 * //If you know the ID of the top most element
 * CompositeElement top = new HierarchyBuilder&lt;MyHierarchyElement>().set(elements).get("00000");
 * //If you do not know the ID of the top most element or
 * //you are to get list of top elements
 * Collection&lt;MyHierarchyElement> tops = new HierarchyBuilder&lt;MyHierarchyElement>().set(elements).get();
 * //You can also get a Hierarchy of the elements
 * Hierarchy&lt;MyHierarchyElement> hierarchy = new HierarchyBuilder&lt;MyHierarchyElement>().set(elements).build();</code></pre>
 * </p>
 * <p>A HierarchyBuilder treats an element as a top HierarchyElement if it meets either of the following conditions.
 * <ul><li>{@link #testTop(Predicate) the predicate you set to the HierarchyBuilder} evaluates a HierarchyInfo to be that from a top element</li>
 *     <li>the element returns HierarchyInfo <a href="HierarchyElement.HierarchyInfo.html#top-element">specifying itself as a top element</a></li>
 * 	   <li>the parent element is not found from the given elements</li>
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
	/**Sets a predicate to test whether a HierarchyElement is a top element by examining the info.
	 * @param test predicate to test whether a HierarchyElement is a top element by examining the info
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
	/**Returns the index of the HierarchyElements used to create the hierarchy
	 * @return index of the HierarchyElements
	 */
	public Map<String, T> getIndex() {
		return index;
	}

	private void add(T lv, T rv) {
		if (lv == null || rv == null || lv == rv) return;

		CompositeElement parent = (CompositeElement)lv;
		parent.add(rv);
	}
	/**Creates a hierarchy and returns its root element.
	 * @param rootID ID of the root object. The object of the ID must be found in the given elements.
	 * @return root element of the created hierarchy
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
	/**Creates a hierarchy and returns a list of top elements.
	 * @return list of top elements
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
	/**Creates and returns a Hierarchy.<br />
	 * The elements are created by calling the {@link #get() get()} method.
	 * @return Hierarchy a Hierarchy
	 */
	public Hierarchy<T> build() {
		return build(Hierarchy::new);
	}
	/**Creates and returns a Hierarchy.<br />
	 * The elements are created by calling the {@link #get() get()} method.
	 * The Hierarchy is created using the given factory.
	 * @param factory supplier of a Hierarchy
	 * @return Hierarchy
	 */
	public <H extends Hierarchy<T>> H build(Supplier<H> factory) {
		Collection<T> tops = get();
		H h = factory.get();
		h.setElements(elements).setIndex(index).setTops(tops);
		return h;
	}
	/**Clears up the internal resources used to build a hierarchy
	 */
	public void clear() {
		if (index != null) {
			index.clear();
			index = null;
		}
		elements = null;
	}
}