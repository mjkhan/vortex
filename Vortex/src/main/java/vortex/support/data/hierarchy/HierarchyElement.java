package vortex.support.data.hierarchy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import vortex.support.AbstractObject;
/**Interface for objects that are part of and build up a hierarchical structure.<br />
 * A HierarchyElement must provide HierarchyInfo that is used by a HierarchyBuilder in building a hierarchy.
 * @author mjkhan
 */
public interface HierarchyElement {
	/**Returns information of the HierarchyElement.
	 * @return information of the HierarchyElement
	 */
	public HierarchyInfo hierarchyInfo();
	/**Returns the parent of the element.<br />
	 * If the element is the root element or the top-most element,<br />
	 * it may return null or the element itself.
	 * @return parent of the element
	 */
	public CompositeElement getParent();
	/**Information on a HierarchyElement that is used by a HierarchyBuilder in creating a hierarchy.<br />
	 * The ID and parent ID from HierarchyInfo don't have to be actual identifiers of objects.<br />
	 * They are good enough as long as the HierarchyBuilder can identify objects using the values.
	 * <p id="top-element">To specify a top HierarchyElement, the parent ID provided in HierarchyInfo should be either
	 * <ul><li>null</li>
	 *     <li>equal to the ID returned by the HierarchyInfo</li>
	 * </ul>
	 * </p>
	 */
	public static class HierarchyInfo extends AbstractObject implements Externalizable {
		private String
			id,
			parentID;
		/**Returns the ID of a HierarchyElement.
		 * @return ID of a HierarchyElement
		 */
		public String ID() {
			return id;
		}
		/**Sets the ID of a HierarchyElement.
		 * @param id ID of a HierarchyElement
		 * @return the HierarchyInfo
		 */
		public HierarchyInfo setID(String id) {
			this.id = id;
			return this;
		}
		/**Returns the parent ID of a HierarchyElement.
		 * @return parent ID of a HierarchyElement
		 */
		public String parentID() {
			return parentID;
		}
		/**Sets the parent ID of a HierarchyElement.
		 * @param parentID parent ID of a HierarchyElement
		 * @return the HierarchyInfo
		 */
		public HierarchyInfo setParentID(String parentID) {
			this.parentID = parentID;
			return this;
		}
		/**Returns whether the HierarchyElement is a top element.<br />
		 * A top element is a HiearchyElement that has no parent element.
		 * @return
		 * <ul><li>true if the element is at top</li>
		 * 	   <li>false otherwise</li>
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