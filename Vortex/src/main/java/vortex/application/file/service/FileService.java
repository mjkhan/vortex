package vortex.application.file.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import vortex.application.file.File;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

public interface FileService {
	public BoundedList<DataObject> search(DataObject req);
	
	public DataObject getInfo(String fileID);
	
	public File getFile(String fileID);
	
	public List<File> getFiles(String... fileIDs);
	
	public List<File> create(MultipartFile... files);
	
	public boolean update(File file);
	
	public int copy(String... fileIDs);
	
	public int remove(String... fileIDs);
}