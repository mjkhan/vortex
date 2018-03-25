package vortex.application.file.service;

import vortex.application.file.File;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

public interface FileService {
	public BoundedList<DataObject> search(DataObject req);
	
	public DataObject getInfo(String fileID);
	
	public File getFile(String fileID);
	
	public boolean create(File file);
	
	public boolean update(File file);
	
	public boolean setStatus(String status, String... fileIDs);
	
	public boolean remove(String... fileIDs);
}