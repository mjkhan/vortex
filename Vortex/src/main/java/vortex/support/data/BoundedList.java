package vortex.support.data;

import java.util.ArrayList;
/**페이징을 위한 정보를 설정하기 위한 리스트.
 * <p>BoundedList는 대량의 요소를 한 번에 원하는 수만큼 제공할 때 사용한다.<br />
 * 예를 들어, 대량의 데이터베이스 조회 결과 데이터를 한 번에 20개씩 제공하고자 할 경우 BoundedList를 사용할 수 있다.</p>
 * <p>이를 위해 개발자는 필요한 수만큼의 데이터를 추출하고 BoundedList에 이 데이터를 담아야 한다.<br />
 * 그리고 나서 BoundedList에 다음 정보를 설정한다.
 * <ul><li>원래 데이터의 {@link #setTotalSize(int) 전체 갯수}</li>
 * 	   <li>{@link #setFetchSize(int) 한 번에 가지고 온 데이터 갯수}</li>
 * 	   <li>현재 가지고 온 데이터의 {@link #setStart(int) 시작 인덱스}(0부터 시작)</li>
 * </ul>
 * <p>BoundedList가 가지고 온 요소를 접근할 때는 0부터 시작하는 인덱스를 사용한다.</p>
 * <p>BoundedList는 {@link #hasMore() 더 가져올 수 있는} 요소가 {@link #hasNext() 앞으로} 또는 {@link #hasPrevious() 뒤로} 있는지 반환한다.</p>
 * @author mjkhan
 * @param <E> 요소 타입
 */
public class BoundedList<E> extends ArrayList<E> {
	private static final long serialVersionUID = 1L;

	private int
		current,
		fetchSize,
		totalSize,
		start;
	/**한 번에 가지고 올 요소의 갯수를 반환한다.
	 * @return 한 번에 가지고 올 요소의 갯수
	 */
	public int getFetchSize() {
		return fetchSize;
	}
	/**한 번에 가지고 올 요소의 갯수를 설정한다.
	 * @param size 한 번에 가지고 올 요소의 갯수
	 * @return BoundedList 자신
	 */
	public BoundedList<E> setFetchSize(int size) {
		if (size < 0)
			throw new IllegalArgumentException("fetchSize < 0");
		this.fetchSize = size;
		return this;
	}
	/**원래 요소의 전체 갯수를 반환한다.
	 * @return 원래 요소의 전체 갯수
	 */
	public int getTotalSize() {
		return isEmpty() ? 0 :
			   totalSize < 1 ? size() : totalSize;
	}
	/**원래 요소의 전체 갯수를 설정한다.
	 * @param totalSize 원래 요소의 전체 갯수
	 */
	public BoundedList<E> setTotalSize(int totalSize) {
		this.totalSize = totalSize;
		return this;
	}
	/**현재 가지고 온 요소의 시작 인덱스를 반환한다.
	 * @return 현재 가지고 온 요소의 시작 인덱스(0부터 시작)
	 */
	public int getStart() {return isEmpty() ? -1 : start;}
	/**현재 가지고 온 요소의 시작 인덱스를 설정한다.
	 * @param start	현재 가지고 온 요소의 시작 인덱스(0부터 시작)
	 */
	public BoundedList<E> setStart(int start) {
		this.start = totalSize < 1 ? -1 : start;
		return this;
	}
	/**현재 가지고 온 요소의 마지막 인덱스를 반환한다.
	 * @return 현재 가지고 온 요소의 마지막 인덱스(0부터 시작)
	 */
	public int getEnd() {
		return isEmpty() ? -1 : start + size() - 1;
	}
	/**현재 선택된 요소의 인덱스를 반환한다.
	 * @return 현재 선택된 요소의 인덱스
	 */
	public int current() {
		return isEmpty() ? -1 : current;
	}
	/**지정하는 인덱스의 요소를 현재 요소로 설정한다.
	 * @param index 요소의 인덱스
	 */
	public BoundedList<E> setCurrent(int index) {
		if (index < 0) {
			current = index;
			return this;
		}
		else if (get(index) != null) current = index;
		return this;
	}
	/**더 가져올 수 있는 요소가 있는지 반환한다.
	 * @return
	 * <ul><li>더 가져올 수 있는 요소가 있으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean hasMore() {
		return !isEmpty() && size() < getTotalSize();
	}
	/**뒤쪽으로 가져올 수 있는 요소가 있는지 반환한다.
	 * @return
	 * <ul><li>가져올 수 있는 요소가 있으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean hasPrevious() {
		return hasMore() && start > 0;
	}
	/**앞쪽으로 가져올 수 있는 요소가 있는지 반환한다.
	 * @return
	 * <ul><li>가져올 수 있는 요소가 있으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean hasNext() {
		return hasMore() && getEnd() < getTotalSize() - 1;
	}
	/**현재 선택된 요소를 반환한다.
	 * @return 현재 선택된 요소
	 */
	public E get() {
		return get(current);
	}
	@Override
	public boolean add(E e) {
		if (e == null || contains(e)) return false;

		boolean result = super.add(e);
		++totalSize;
		return result;
	}
	@Override
	public void add(int index, E e) {
		super.add(index, e);
		++totalSize;
	}
	@Override
	public E remove(int index) {
		E e = super.remove(index);
		if (e != null)
			--totalSize;
		return e;
	}
	@Override
	public void clear() {
		super.clear();
		totalSize  = 0;
		start = -1;
	}
	/**BoundedList를 초기화 한다.
	 */
	public BoundedList<E> init() {
		totalSize = size();
		return this;
	}
	/**페이징 처리를 위한 값들을 계산하기 위한 유틸리티*/
	public static class Fetch {
		public static final int
			ALL = 0,
			NONE = -1;

		private Fetch() {}
		/**Returns the number of fetches needed to provide all elements.
		 * @param elementCount	number of all elements
		 * @param size			size or number of elements in a fetch
		 * @return number of fetches needed to provide all elements
		 */
		public static final int count(int elementCount, int size) {
			if (elementCount == 0 || size == ALL) return 1;
			return (elementCount / size) + ((elementCount % size) == 0 ? 0 : 1);
		}
		/**Returns the end index of the elements starting from the start index.
		 * @param elementCount	number of all elements
		 * @param size			size or number of elements in a fetch
		 * @param start 		0-based start index
		 * @return 0-based end index of the
		 */
		public static final int end(int elementCount, int size, int start) {
			if (size < ALL)
				throw new IllegalArgumentException("Invalid size: " + size);
			if (elementCount < 0)
				throw new IllegalArgumentException("Invalid elementCount: " + elementCount);

			int last = elementCount - 1;
			if (size == ALL) return last;
			return Math.min(last, start + size -1);
		}
		/**
		 * @param current
		 * @param count
		 * @return
		 */
		public static final int page(int current, int count) {
			return count < 1 ? 0 : current / count;
		}
		/**
		 * @param page
		 * @param visibleLinks
		 * @return
		 */
		public static final int band(int page, int visibleLinks) {
			return visibleLinks < 1 ? 0 : page / visibleLinks;
		}
	}

}