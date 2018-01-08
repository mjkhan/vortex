package vortex.application;

import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

public abstract class EntityMapper<T> extends DataMapper {
	protected abstract String namespace();
	protected abstract String entityName();
	
	
	public BoundedList<DataObject> search(DataObject params) {
		if (isEmpty(params.get("value"))) {
			params.remove("value");
			params.remove("field");
		}
		return boundedList(
			selectList(namespace() + ".search", params)
		  , params
		);
	}
	
	public DataObject getInfo(String id) {
		return selectOne(namespace() + ".getInfo", id);
	}
	
	public T getObject(String id) {
		return selectOne(namespace() + ".get" + entityName(), id);
	}
	
	public boolean create(T obj) {
		return obj != null ?
			insert(namespace() + ".insert", params(true).set(entityName().toLowerCase(), obj)) == 1
		   :false;
	}
	
	public boolean update(T obj) {
		return obj != null ?
			insert(namespace() + ".update", params(true).set(entityName().toLowerCase(), obj)) == 1
		   :false;
	}
	
	public int setStatus(String status, String... objIDs) {
		return update(
			namespace() + ".setStatus"
		   ,params(true)
			.set(entityName().toLowerCase() + "IDs", objIDs)
			.set("status", status)
		);
	}
	
	public int remove(String... objIDs) {
		return setStatus(Status.REMOVED.code(), objIDs);
	}
	
	public int delete(String... objIDs) {
		return delete(
			namespace() + ".delete"
		   ,params().set(entityName() + "IDs", objIDs)
		);
	}
}