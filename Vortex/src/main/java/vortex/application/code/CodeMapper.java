package vortex.application.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vortex.support.data.DataObject;
import vortex.support.database.AbstractMapper;

public class CodeMapper extends AbstractMapper {
	public Map<String, List<DataObject>> getCodes(String... groupIDs) {
		List<DataObject> codes = selectList("code.getCodes", params().set("groupIDs", groupIDs));
		if (codes.isEmpty()) return Collections.emptyMap();
		
		LinkedHashMap<String, List<DataObject>> result = new LinkedHashMap<>();
		codes.forEach(row -> {
			String groupID = row.string("grp_id");
			List<DataObject> tmp = result.computeIfAbsent(groupID, key -> new ArrayList<DataObject>());
			tmp.add(row);
		});
	
		return result;
	}
	
	public Code getCode(String groupID, String code) {
		return selectOne(
			"code.getCode"
		   , params().set("groupID", groupID)
		   			 .set("code", code)
		);
	}
	
	public int create(Code code) {
		return insert("code.insert", code);
	}
	
	public int update(Code code) {
		return update("code.update", code);
	}
	
	public int deleteCodes(String groupID, String... codes) {
		return delete(
			"code.delete"
		   , params().set("groupID", groupID)
		  			 .set("codes", !isEmpty(codes) ? codes : null)
		);
	}
}