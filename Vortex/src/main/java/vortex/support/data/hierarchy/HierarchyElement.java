package vortex.support.data.hierarchy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import vortex.support.AbstractObject;
/**계층 구조를 구성하는 객체들이 구현하는 인터페이스.<br />
 * HierarchyElement는 HierarchyInfo를 제공한다. HierarchyInfo는 HierarchyBuilder가 계층 구조를 구축하는데 사용한다.
 * @author mjkhan
 */
public interface HierarchyElement {
	/**HierarchyElement의 정보를 반환한다.
	 * @return HierarchyElement 정보
	 */
	public HierarchyInfo hierarchyInfo();
	/**객체의 상위(부모) 객체를 반환한다.<br />
	 * 객체가 최상위(루트) 객체거나 여러 최상위 객체들 중 하나일 경우,<br />
	 * null이나 자기 자신을 반환한다.
	 * @return 객체의 상위 객체
	 */
	public CompositeElement getParent();
	/**HierarchyElement의 정보로, HierarchyBuilder가 계층 구조를 구축할 때 사용한다.<br />
	 * HierarchyInfo의 ID와 parentID는 객체의 실제 식별자일 필요는 없다.<br />
	 * ID와 parentID는 HierarchyBuilder가 객체들을 구분할 수 있기만 하면 된다.
	 * <p id="top-element">HierarchyElement가 최상위 객체로 인식되려면, HierarchyInfo의 parentID는
	 * <ul><li>null, 또는</li>
	 *     <li>HierarchyInfo의 ID와 같은 값</li>
	 * </ul>
	 * 이어야 한다.
	 * </p>
	 */
	public static class HierarchyInfo extends AbstractObject implements Externalizable {
		private String
			id,
			parentID;
		/**HierarchyElement의 ID를 반환한다.
		 * @return HierarchyElement의 ID
		 */
		public String ID() {
			return id;
		}
		/**HierarchyElement의 ID를 설정한다.
		 * @param id HierarchyElement의 ID
		 * @return HierarchyInfo
		 */
		public HierarchyInfo setID(String id) {
			this.id = id;
			return this;
		}
		/**HierarchyElement의 상위 객체의 ID.
		 * @return HierarchyElement의 상위 객체의 ID
		 */
		public String parentID() {
			return parentID;
		}
		/**HierarchyElement의 상위 객체의 ID를 설정한다.
		 * @param parentID HierarchyElement의 상위 객체의 ID
		 * @return HierarchyInfo
		 */
		public HierarchyInfo setParentID(String parentID) {
			this.parentID = parentID;
			return this;
		}
		/**HierarchyElement가 최상위 요소인지 반환한다.<br />
		 * 최상위 요소란 상위 요소가 없는 HiearchyElement를 말한다.
		 * @return
		 * <ul><li>요소가 최상위이면 true</li>
		 * 	   <li>그렇지 않으면 false</li>
		 * </ul>
		 */
		public boolean atTop() {
			return isEmpty(parentID) || equals(notEmpty(id, "id"), parentID);
		}
		@Override
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			id = ifEmpty(in.readUTF(), null);
			parentID = ifEmpty(in.readUTF(), null);
		}
		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeUTF(ifEmpty(id, ""));
			out.writeUTF(ifEmpty(parentID, ""));
		}
		@Override
		public String toString() {
			return getClass().getName() + "(id: '" + id + "', parentID: '" + parentID + "')";
		}
	}
}