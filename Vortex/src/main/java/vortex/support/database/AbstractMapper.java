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

	/**{@link vortex.support.Assert#notEmpty(Object, String) Assert.notEmpty(...)} 참고
	 */
	protected static <T> T notEmpty(T t, String s) {
		return Assert.notEmpty(t, s);
	}
	
	/**{@link vortex.support.Asser#runtimeException(Throwable) Assert.runtimeException(...)} 참고
	 */
	protected static RuntimeException runtimeException(Throwable t) {
		return Assert.runtimeException(t);
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
					int totalCount = selectOne("foundRows");
					result.setTotalSize(totalCount)
						  .setStart(start)
						  .setFetchSize(fetch);
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
		req.putIfAbsent("start", 0);
		req.putIfAbsent("fetch", 0);
		
		int start = req.number("start").intValue(),
			fetch = req.number("fetch").intValue();
		return boundedList(list, start, fetch);
	}
	
	/**파라미터를 담을 DataObject를 생성하여 반환한다.
	 * @return DataObject
	 */
	protected DataObject params() {
		return new DataObject();
	}
	
	/**현재 클래스의 Log를 반환한다.
	 * @return 현재 클래스의 Log
	 */
	protected Log log() {
		return Log.get(getClass());
	}
}