package vortex.support.data.hierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import vortex.support.AbstractObject;

/**Interface for objects that have child HierarchyElements.
 * @author mjkhan
 */
public interface CompositeElement extends HierarchyElement {
	/**Adds e as a child
	 * @param e child element
	 */
	public void add(HierarchyElement e);
	/**Returns child elements.
	 * @return child elements
	 */
	public Collection<? extends HierarchyElement> getChildren();
	/**CompositeElement utility
	 */
	public static class Support extends AbstractObject {
		/**Returns whether dsc is a branch of asc.<br />
		 * That is, it tests whether asc is an ancestor of dsc and dsc is a descendent of asc on a hierarchy branch.
		 * @param asc ancestor element
		 * @param dsc descendent element
		 * @return
		 * <ul><li>true if dsc is a branch of asc</li>
		 * 	   <li>false otherwise</li>
		 * </ul>
		 */
		public static final boolean ofBranch(CompositeElement asc, CompositeElement dsc) {
			if (asc == null || dsc == null) return false;

			CompositeElement parent = dsc.getParent();
			return asc.equals(parent)
				|| ofBranch(asc, parent);
		}
		/**Returns IDs of the elements and their child CompositeElements.
		 * @param elements CompositeElements
		 * @return IDs of the elements and their child CompositeElements
		 */
		public static final List<String> getIDs(Iterable<? extends CompositeElement> elements) {
			if (isEmpty(elements))
				return Collections.emptyList();

			ArrayList<String> result = new ArrayList<String>();
			elements.forEach(element -> {
				if (element == null) return;
				result.add(element.hierarchyInfo().ID());
				ArrayList<CompositeElement> children = null;
				for (HierarchyElement child: element.getChildren()) {
					if (!(child instanceof CompositeElement)) continue;
					if (children == null)
						children = new ArrayList<CompositeElement>();
					children.add(CompositeElement.class.cast(child));
				}
				if (isEmpty(children)) return;
				result.addAll(getIDs(children));
			});
			return result;
		}

		private static void toString(StringBuilder buff, HierarchyElement element, int level) {
			if (level > 0) {
				for (int i = 0; i < level; ++i)
					buff.append("  ");
				buff.append("+").append("-");
			}

			buff.append(element);
		}
		/**Returns the string representation of the elements and their children.
		 * @param elements CompositeElements
		 * @return string representation of the elements and their children
		 */
		public static final String toString(Iterable<? extends CompositeElement> elements) {
			if (isEmpty(elements)) return "";
			
			return new Converter()
				.setDelimiter("\n")
				.beginElement(Support::toString)
				.convert(elements);
		}
	}
	
	public static class Converter extends AbstractObject {
		@FunctionalInterface
		public static interface Stringify {
			public void set(StringBuilder buff, HierarchyElement element, int level);
		}
		
		private static final Stringify emptyString = (buff, element, level) -> {};
		
		private String delimiter = "";
		private Stringify
			beginElement = emptyString,
			endElement = emptyString,
			beginChildren = emptyString,
			endChildren = emptyString;

		public Converter setDelimiter(String delimiter) {
			this.delimiter = delimiter;
			return this;
		}
		
		public Converter beginElement(Stringify begin) {
			this.beginElement = begin;
			return this;
		}
		
		public Converter endElement(Stringify end) {
			this.endElement = end;
			return this;
		}
		
		public Converter beginChildren(Stringify begin) {
			this.beginChildren = begin;
			return this;
		}
		
		public Converter endChildren(Stringify end) {
			this.endChildren = end;
			return this;
		}

		private void convert(Iterable<? extends HierarchyElement> elements, StringBuilder buff, int level) {
			elements.forEach(element -> {
				if (element == null) return;
				if (buff.length() > 0)
					buff.append(delimiter);
				
				boolean parent = element instanceof CompositeElement;
				Collection<? extends HierarchyElement> children = parent ? ((CompositeElement)element).getChildren() : null;
				parent = parent && !isEmpty(children);
				
				beginElement.set(buff, element, level);
				if (parent) {
					int sublevel = level + 1;
					beginChildren.set(buff, element, sublevel);
					convert(children, buff, sublevel);
					endChildren.set(buff, element, sublevel);;
				}
				endElement.set(buff, element, level);
			});
		}
		
		public String convert(Iterable<? extends HierarchyElement> elements) {
			if (isEmpty(elements)) return "";

			StringBuilder buff = new StringBuilder();
			convert(elements, buff, 0);
			return buff.toString();
		}
	}
}