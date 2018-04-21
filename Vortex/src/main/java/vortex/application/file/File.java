package vortex.application.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import vortex.support.Assert;

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
	
	public static final String dir(String path) {
		String name = name(path);
		if (name.isEmpty()) return path;
		
		return path.replace(name, "");
	}
	
	public static final String name(String path) {
		if (path == null || path.trim().isEmpty()) return "";
		int pos = path.lastIndexOf("/");
		if (pos < 0)
			pos = path.lastIndexOf("\\");
		if (pos < 0) return path;
		return path.substring(pos + 1, path.length());
	}
	
	public static final String ext(String filename) {
		if (filename == null || filename.trim().isEmpty()) return "";
		int pos = filename.lastIndexOf(".");
		if (pos < 1) return "";
		return filename.substring(pos + 1, filename.length());
	}
	
	public static final void mkdirs(String path) {
		java.io.File d = new java.io.File(path);
		if (!d.exists())
			d.mkdirs();
	}
	
	public static final void mkdirs(Collection<File> files, String prefix) {
		if (files == null || files.isEmpty()) return;
		
		files.stream()
			 .map(file -> File.dir(prefix + file.getPath()))
			 .distinct()
			 .forEach(File::mkdirs);
	}
	
	public static void zip(String zipPath, Iterable<File> files) {
		try (
			FileOutputStream fos = new FileOutputStream(zipPath);
			ZipOutputStream zos = new ZipOutputStream(fos);
			) {
			for (File file: files) {
				String filename = file.getName();
				ZipEntry entry = new ZipEntry(filename);
				zos.putNextEntry(entry);
				FileInputStream fis = file.getInputStream();
				byte[] data = new byte[1024];
				int length = -1;
				while ((length = fis.read(data)) >= 0) {
					zos.write(data, 0, length);
				}
				fis.close();
			}
			zos.closeEntry();
		} catch (Exception e) {
			throw Assert.runtimeException(e);
		}
	}
	
	public void copy(String path) {
		try {
			Path src = Paths.get(location),
				 dest = Paths.get(path);
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw Assert.runtimeException(e);
		}
	}
	
	public void move(String path) {
		try {
			Path src = Paths.get(location),
				 dest = Paths.get(path);
			Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw Assert.runtimeException(e);
		}
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

	public FileInputStream getInputStream() {
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
		Arrays.asList("abc.txt", "def", ".ghi", "jkl.mno.pqr").forEach(s -> System.out.println("[" + ext(s) + "]"));
		Arrays.asList("abc.txt", "/def", "/111/222.ghi", "jkl.mno.pqr", "\\stu\\vw.xyz").forEach(s -> System.out.println("[" + name(s) + "]"));
		Arrays.asList("abc.txt", "/def", "/111/222.ghi", "jkl.mno.pqr", "\\stu\\vw.xyz").forEach(s -> System.out.println("[" + dir(s) + "]"));
	}
*/
}