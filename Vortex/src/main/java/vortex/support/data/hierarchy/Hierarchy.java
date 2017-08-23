package vortex.support.data.hierarchy;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import vortex.support.AbstractObject;
/**Is a collection of HierarchyElements and is created by a {@link HierarchyBuilder HierarchyBuilder}.
 * With a Hierarchy, you can get
 * <ul><li>{@link #getElements() elements of the Hierarchy}</li>
 *     <li>{@link #getIndex() index of the elements}</li>
 *     <li>{@link #topElements() top elements}</li>
 *     <li>{@link #getTop() root element}</li>
 *     <li>{@link #get(String) an element} with an ID</li>
 * </ul>
 * @author mjkhan
 * @param <T> a type of HierarchyElement
 */
public class Hierarchy<T> extends AbstractObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private Collection<T>
		elements,
		tops;
	private Map<String, T> index;
	/**Returns whether the Hierarchy is empty.
	 * @return
	 * <ul><li>true if the Hierarchy is empty</li>
	 * 	   <li>false otherwise</li>
	 * </ul>
	 */
	public boolean isEmpty() {
		return isEmpty(index) || isEmpty(elements);
	}
	/**Returns the index of the Hierarchy's elements.
	 * @return index of the Hierarchy's elements
	 */
	public Map<String, T> getIndex() {
		return index;
	}

	Hierarchy<T> setIndex(Map<String, T> index) {
		this.index = index;
		return this;
	}
	/**Returns the elements of the Hierarchy.
	 * @return elements of the Hierarchy
	 */
	public Collection<T> getElements() {
		return elements;
	}

	Hierarchy<T> setElements(Collection<T> elements) {
		this.elements = elements;
		return this;
	}
	/**Returns the top elements of the Hierarchy.
	 * @return top elements of the Hierarchy
	 */
	public Collection<T> topElements() {
		return tops;
	}
	/**Returns the first top or root element of the Hierarchy.
	 * @return root element of the Hierarchy
	 */
	public T getTop() {
		return !isEmpty() ? tops.iterator().next() : null;
	}

	Hierarchy<T> setTops(Collection<T> tops) {
		this.tops = tops;
		return this;
	}
	/**Returns the HierarchyElement with the given elementID.
	 * @param elementID
	 * @return HierarchyElement with the given elementID
	 */
	public T get(String elementID) {
		return !isEmpty() ? index.get(elementID) : null;
	}
}