package vortex.application.file.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

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
	
	protected File convert(MultipartFile upload) {
		File file = new File();
		file.setType(fileType);
		file.setName(File.name(upload.getOriginalFilename()));
		file.setContentType(upload.getContentType());
		file.setSize(upload.getSize());
		return file;
	}
	
	public List<File> create(MultipartFile... uploads) {
		if (isEmpty(uploads))
			return Collections.emptyList();
		
		LinkedHashMap<MultipartFile, File> files = new LinkedHashMap<>();
		for (MultipartFile upload: uploads) {
			files.put(upload, convert(upload));
		}
		DataObject params = params(true);
		try {
			//먼저 INSERT
			files.values().forEach(file ->
				insert(
					"file.insert",
					params.set("file", file)
						  .set("ext", ifEmpty(File.ext(file.getName()), null))
				)
			);
			
			//dir 추출 및 생성
			files.values().stream().map(file -> {
				String location = pathPrefix + file.getPath(),
					   filename = File.name(location);
				return location.replace(filename, "");
			}).distinct().forEach(dir -> {
				java.io.File d = new java.io.File(dir);
				if (!d.exists())
					d.mkdirs();
			});
			
			// 파일 저장
			for (Map.Entry<MultipartFile, File> entry: files.entrySet()) {
				File file = entry.getValue();
				String path = pathPrefix + file.getPath();
				entry.getKey().transferTo(new java.io.File(path));
			}
			
			return new ArrayList<File>(files.values());
		} catch (Exception e) {
			files.values().forEach(this::delete);
			throw runtimeException(e);
		}
	}
	
	private void delete(File file) {
		java.io.File target = new java.io.File(pathPrefix + file.getPath());
		if (target.exists())
			target.delete();
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