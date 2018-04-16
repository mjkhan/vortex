package vortex.application.file.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vortex.application.ApplicationService;
import vortex.application.file.File;
import vortex.support.data.BoundedList;
import vortex.support.data.DataObject;

@Service("fileService")
public class FileServiceImpl extends ApplicationService implements FileService {
	@Resource(name="genericFile")
	private FileMapper fileMapper;

	@Override
	public BoundedList<DataObject> search(DataObject req) {
		return fileMapper.search(req);
	}

	@Override
	public DataObject getInfo(String fileID) {
		return fileMapper.getInfo(fileID);
	}

	@Override
	public File getFile(String fileID) {
		return fileMapper.getFile(fileID);
	}

	@Override
	public List<File> create(MultipartFile... files) {
		return fileMapper.create(files);
	}

	@Override
	public boolean update(File file) {
		return fileMapper.update(file);
	}

	@Override
	public int copy(String... fileIDs) {
		return fileMapper.copy(fileIDs);
	}

	@Override
	public int remove(String... fileIDs) {
		return fileMapper.remove(fileIDs);
	}
}