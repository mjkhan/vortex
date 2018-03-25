package vortex.application.file.service;

import java.util.List;

import vortex.application.DataMapper;
import vortex.application.file.File;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;
import vortex.support.data.Status;

public class FileMapper extends DataMapper {
	private String
		fileType,
		pathPrefix,
		urlPrefix;
	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}
	
	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
	
	@Override
	protected DataObject params(boolean currentUser) {
		return super.params(currentUser)
					.set("fileType", fileType)
					.set("pathPrefix", pathPrefix)
					.set("urlPrefix", urlPrefix);
	}
	
	public BoundedList<DataObject> search(DataObject req) {
		DataObject params = ifEmpty(req, this::params);
		if (isEmpty(req.get("searchBy")))
			req.remove("searchBy");
		if (isEmpty(req.get("searchTerms")))
			req.remove("searchTerms");
		List<DataObject> list = selectList(
			"file.search",
			params.set("fileType", fileType)
				  .set("pathPrefix", pathPrefix)
				  .set("urlPrefix", urlPrefix)
		);
		return boundedList(list, params);
	}
	
	public DataObject getInfo(String fileID) {
		return selectOne(
			"file.getInfo"
			,params().set("fileID", fileID)
		);
	}
	
	public File getFile(String fileID) {
		return selectOne(
			"file.getFile"
			,params().set("fileID", fileID)
		);
	}
	
	public boolean create(File file) {
		if (file == null) return false;
		
		file.setType(fileType);
		return insert("file.insert", params(true).set("file", file)) == 1;
	}
	
	public boolean update(File file) {
		if (file == null) return false;
		
		file.setType(fileType);
		return insert("file.update", params(true).set("file", file)) == 1;
	}
	
	public int setStatus(String status, String... fileIDs) {
		return update(
			"file.setStatus"
		   , params(true)
		   		.set("fileIDs", fileIDs)
		   		.set("status", status)
		);
	}
	
	public int remove(String... fileIDs) {
		return  setStatus(Status.REMOVED.code(), fileIDs);
	}
}