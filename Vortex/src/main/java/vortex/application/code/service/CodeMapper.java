package vortex.application.code.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import vortex.application.DataMapper;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

@Repository("codeMapper")
public class CodeMapper extends DataMapper {
	public List<DataObject> getCodes(String... groupIDs) {
		return selectList("code.getCodes", params().set("groupIDs", groupIDs));
	}
	
	public BoundedList<DataObject> getCodes(DataObject params) {
		String groupID = (String)params.remove("groupID");
		params.put("groupIDs", groupID.split(","));
		return boundedList(selectList("code.getCodes", params), params);
	}
/*	
	public Map<String, List<DataObject>> getCodesOf(String... groupIDs) {
		return getCodes(groupIDs).stream().collect(
			Collectors.groupingBy(row -> row.string("grp_id"))
		);
	}
*/	
	public DataObject getInfo(String groupID, String code) {
		return selectOne(
			"code.getInfo"
		   , params().set("groupID", groupID)
		   			 .set("code", code)
		);
	}
	
	public Code getCode(String groupID, String code) {
		return selectOne(
			"code.getCode"
		   , params().set("groupID", groupID)
		   			 .set("code", code)
		);
	}
	
	public boolean create(Code code) {
		return code != null ? insert("code.insert", params(true).set("code", code)) == 1
			 : false;
	}
	
	public boolean update(Code code) {
		return code != null ? update("code.update", params(true).set("code", code)) == 1
			 : false;
	}
	
	public int setStatus(String status, String groupID, String... codes) {
		return update(""
			, params(true)
				.set("groupID", groupID)
				.set("status", status)
				.set("codes", !isEmpty(codes) ? codes : null)
		);
	}
	
	public int remove(String groupID, String... codes) {
		return setStatus(Status.REMOVED.code(), groupID, codes);
	}
	
	public int deleteCodes(String groupID, String... codes) {
		return delete(
			"code.delete"
		   , params().set("groupID", groupID)
		  			 .set("codes", !isEmpty(codes) ? codes : null)
		);
	}
}