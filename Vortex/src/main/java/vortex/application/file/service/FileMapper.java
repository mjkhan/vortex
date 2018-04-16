package vortex.application.file.service;

import java.util.ArrayList;
import java.util.Arrays;
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
		List<DataObject> list = selectList(
			"file.getInfo"
		    ,params().set("fileIDs", new String[]{fileID})
		);
		return !list.isEmpty() ? list.get(0) : null;
	}
	
	public List<File> getFiles(String... fileIDs) {
		return selectList(
			"file.getFile"
		    ,params().set("fileIDs", fileIDs)
		);
	}
	
	public File getFile(String fileID) {
		List<File> files = getFiles(fileID);
		return !files.isEmpty() ? files.get(0) : null;
	}
	
	protected File convert(MultipartFile upload) {
		String filename = upload.getOriginalFilename();
		if (isEmpty(filename))
			return null;
		File file = new File();
		file.setType(fileType);
		file.setName(filename);
		file.setContentType(upload.getContentType());
		file.setSize(upload.getSize());
		return file;
	}
	
	public List<File> create(MultipartFile... uploads) {
		if (isEmpty(uploads))
			return Collections.emptyList();
		
		LinkedHashMap<MultipartFile, File> files = new LinkedHashMap<>();
		for (MultipartFile upload: uploads) {
			File file = convert(upload);
			if (file != null)
				files.put(upload, file);
		}
		try {
			ArrayList<File> list = new ArrayList<>(files.values());
			//먼저 INSERT
			insert(list);
			
			File.mkdirs(list, pathPrefix);
			for (Map.Entry<MultipartFile, File> entry: files.entrySet()) {
				File file = entry.getValue();
				String path = pathPrefix + file.getPath();
				entry.getKey().transferTo(new java.io.File(path));
			}
			
			return list;
		} catch (Exception e) {
			files.values().forEach(this::delete);
			throw runtimeException(e);
		}
	}
	
	protected int insert(File... files) {
		return insert(Arrays.asList(files));
	}
	
	protected int insert(Iterable<File> files) {
		DataObject params = params(true);
		int affected = 0;
		for (File file: files) {
			affected += insert(
				"file.insert",
				params.set("file", file)
					  .set("ext", ifEmpty(File.ext(file.getName()).toLowerCase(), null))
			);
		}
		return affected;
	}
	
	protected void delete(File file) {
		java.io.File target = new java.io.File(pathPrefix + file.getPath());
		if (target.exists())
			target.delete();
	}
	
	public boolean update(File file) {
		if (file == null) return false;
		
		file.setType(fileType);
		return insert("file.update", params(true).set("file", file)) == 1;
	}
	
	public int copy(String... fileIDs) {
		return copy(getFiles(fileIDs));
	}
	
	public int copy(Iterable<File> srcs) {
		if (isEmpty(srcs)) return 0;
		
		LinkedHashMap<File, File> map = new LinkedHashMap<>();
		for (File src: srcs) {
			File file = new File();
			file.setType(fileType);
			file.setName(src.getName());
			file.setContentType(src.getContentType());
			file.setDescription(src.getDescription());
			file.setSize(src.getSize());
			map.put(src, file);
		}
		try {
			int affected = insert(map.values());
			
			File.mkdirs(map.values(), pathPrefix);
			map.entrySet().forEach(entry -> {
				File src = entry.getKey();
				src.copy(pathPrefix + entry.getValue().getPath());					
			});
			
			return affected;
		} catch (Exception e) {
			map.values().forEach(this::delete);
			throw runtimeException(e);
		}
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
		List<File> files = getFiles(fileIDs);
		int affected = setStatus(Status.REMOVED.code(), fileIDs);
		if (affected > 0) {
			String dir = pathPrefix + "removed/";
			File.mkdirs(files, dir);
			files.forEach(src -> src.move(dir + src.getPath()));
		}
		return affected;
	}
}