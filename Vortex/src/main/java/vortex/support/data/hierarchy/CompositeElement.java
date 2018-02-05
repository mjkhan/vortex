package vortex.support.data.hierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import vortex.support.AbstractObject;

/**하위 HierarchyElement를 갖는 오브젝트를 위한 Interface.
 * @author mjkhan
 */
public interface CompositeElement extends HierarchyElement {
	/**e를 하위요소로 추가한다.
	 * @param e 하위요소
	 */
	public void add(HierarchyElement e);
	/**하위요소들을 반환한다.
	 * @return 하위요소들
	 */
	public Collection<? extends HierarchyElement> getChildren();
	/**CompositeElement 유틸리티
	 */
	public static class Support extends AbstractObject {
		/**dsc가 asc의 하위요소인지를 반환한다.<br />
		 * 즉, asc가 dsc의 상위요소이고 dsc가 asc의 하위요소인지 반환한다.
		 * @param asc 상위요소
		 * @param dsc 하위요소
		 * @return
		 * <ul><li>dsc가 asc의 하위요소이면 true</li>
		 * 	   <li>그렇지 않으면 false</li>
		 * </ul>
		 */
		public static final boolean ofBranch(CompositeElement asc, CompositeElement dsc) {
			if (asc == null || dsc == null) return false;

			CompositeElement parent = dsc.getParent();
			return asc.equals(parent)
				|| ofBranch(asc, parent);
		}
		/**elements와 각 하위 CompositeElement들의 ID를 반환한다.
		 * @param elements CompositeElements
		 * @return elements와 각 하위 CompositeElement들의 ID
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

		private static String toString(HierarchyElement element, int level) {
			String result = "\n";
			if (level > 0) {
				result += Stringify.indent("  ", level) + "+-";
			}

			return result += element.toString();
		}
		/**elements와 그 하위요소들을 String으로 변환하여 반환한다.
		 * @param elements CompositeElements
		 * @return string elements와 그 하위요소들의 String 표현
		 */
		public static final <T extends HierarchyElement> String toString(Iterable<T> elements) {
			if (isEmpty(elements)) return "";
			
			return new Stringify<T>()
				.beginElement(Support::toString)
				.toString(elements);
		}
	}
}