package vortex.application.file;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;

public class File {
	private String
		id,
		type,
		name,
		path,
		location,
		url,
		contentType,
		description,
		createdBy,
		status;
	private long size;
	private Date createdAt;
	
	public static final String name(String path) {
		if (path == null || path.trim().isEmpty()) return "";
		int pos = path.lastIndexOf("\\");
		if (pos < 0)
			pos = path.lastIndexOf("/");
		if (pos < 0) return path;
		return path.substring(pos + 1, path.length());
	}
	
	public static final String ext(String filename) {
		if (filename == null || filename.trim().isEmpty()) return "";
		int pos = filename.lastIndexOf(".");
		if (pos < 1) return "";
		return filename.substring(pos + 1, filename.length());
	}
	
	public void copy(String path) throws Exception {
		Path src = Paths.get(location),
			 dest = Paths.get(path);
		Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public FileInputStream getStream() {
		try {
			return location == null ? null : new FileInputStream(location);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("%s{%s, %s}", getClass().getName(), id, name);
	}
/*
	public static void main(String[] args) {
//		Arrays.asList("abc.txt", "def", ".ghi", "jkl.mno.pqr").forEach(s -> System.out.println("[" + ext(s) + "]"));
		Arrays.asList("abc.txt", "/def", "/111/222.ghi", "jkl.mno.pqr", "\\stu\\vw.xyz").forEach(s -> System.out.println("[" + name(s) + "]"));
	}
*/
}