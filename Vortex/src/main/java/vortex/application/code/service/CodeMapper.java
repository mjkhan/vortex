package vortex.application.code.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import vortex.application.DataMapper;
import vortex.support.data.DataObject;

@Repository("codeMapper")
public class CodeMapper extends DataMapper {
	public List<DataObject> getCodes(String... groupIDs) {
		return selectList("code.getCodes", params().set("groupIDs", groupIDs));
	}
	
	public Map<String, List<DataObject>> getCodesOf(String... groupIDs) {
		List<DataObject> codes = getCodes(groupIDs);
		if (codes.isEmpty()) return Collections.emptyMap();
		
		LinkedHashMap<String, List<DataObject>> result = new LinkedHashMap<>();
		codes.forEach(row -> 
			result.computeIfAbsent(row.string("grp_id"), key -> new ArrayList<DataObject>())
				.add(row)
		);
	
		return result;
	}
	
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
	
	public int deleteCodes(String groupID, String... codes) {
		return delete(
			"code.delete"
		   , params().set("groupID", groupID)
		  			 .set("codes", !isEmpty(codes) ? codes : null)
		);
	}
}