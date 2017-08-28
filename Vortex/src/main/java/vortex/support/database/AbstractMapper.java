package vortex.support.database;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.ibatis.session.SqlSession;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;
import vortex.support.Assert;
import vortex.support.Log;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

/**SqlSession을 사용하는 SqlMapper의 베이스 클래스 
 * @author mjkhan
 */
public class AbstractMapper extends EgovAbstractMapper {

	/**{@link vortex.support.Assert#isEmpty(Object) Assert.isEmpty(Object)} 참고
	 */
	protected static boolean isEmpty(Object obj) {
		return Assert.isEmpty(obj);
	}
	
	/**{@link vortex.support.Assert#ifEmpty(Object, Supplier) Assert.ifEmpty(...)} 참고
	 */
	protected static <T> T ifEmpty(T t, Supplier<T> nt) {
		return Assert.ifEmpty(t, nt);
	}

	protected SqlSession getSqlSession(String name) {
		return SqlSessionDispenser.get(name);
	}
	
	/**list를 BoundedList로 옮겨담고 start, fetchSize, totalSize등을 설정한 후 반환한다.
	 * @param list  리스트
	 * @param start 리스트 요소의 시작 인덱스
	 * @param fetch 한 번에 가져온 요소들의 수
	 * @return BoundedList
	 */
	protected <E> BoundedList<E> boundedList(List<E> list, int start, int fetch) {
		BoundedList<E> result = new BoundedList<>();
		if (!isEmpty(list)) {
			result.addAll(list);
			
			if (fetch > 0) {
				Object e = list.get(0);
				if (e instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, Object> row = (Map<String, Object>)e;
					Number totalCount = (Number)row.get("TOTAL_ROW_CNT");
					if (totalCount != null)
						result.setTotalSize(totalCount.intValue());
					result.setStart(start).setFetchSize(fetch);
				}
			}
		}
		return result;
	}
	
	/**list를 BoundedList로 옮겨담고 start, fetchSize, totalSize등을 설정한 후 반환한다.
	 * @param list  리스트
	 * @param start 리스트 요소의 시작 인덱스
	 * @param fetchSize 한 번에 가져온 요소들의 수
	 * @return BoundedList
	 */
	protected <E> BoundedList<E> boundedList(List<E> list, DataObject req) {
		int start = req.number("start").intValue(),
			fetch = req.number("fetch").intValue();
		return boundedList(list, start, fetch);
	}
	
	protected DataObject params() {
		return new DataObject();
	}
	
	protected Log log() {
		return Log.get(getClass());
	}
}