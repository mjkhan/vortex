package vortex.support.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import vortex.support.AbstractObject;
/**파일 처리를 위한 유틸리티.
 * <p>FileSupport는 <code>new</code> constructor나 {@link #get() 싱글턴}으로 생성할 수 있다.<br />
 * FileSupport는
 * <ul><li>{@link #create(String, byte[], String) 파일}이나 {@link #create(String) 디렉토리}를 생성한다.</li>
 *     <li>{@link #rename(String, String) 파일}이나 {@link #move(String, String) 디렉토리}의 이름을 변경한다.</li>
 *     <li>파일이나 디렉토리를 {@link #delete(String...) 삭제}한다.</li>
 *     <li>파일이나 디렉토리를{@link #copy(String, String) 복사}한다.</li>
 * </ul>
 * </p>
 * @author mjkhan
 */
public class FileSupport extends AbstractObject {
	private static FileSupport obj;
	/**싱글턴 FileSupport를 반환한다.
	 * @return 싱글턴 FileSupport
	 */
	public static final FileSupport get() {
		return obj != null ? obj : (obj = new FileSupport());
	}
	/**주어진 파일 경로에서 파일 이름을 추출하여 반환한다.
	 * @param filepath		파일 경로
	 * @param separator 파일 구분자. 공백일 경우 OS의 파일 구분자를 사용한다.
	 * @return 파일 이름
	 */
	public static final String fileName(String filepath, String separator) {
		int index = filepath.lastIndexOf(ifEmpty(separator, File.separator));
		return index < 0 ? filepath : filepath.substring(index + 1, filepath.length());
	}
	/**주어진 파일 경로에서 확장자를 추출하여 반환한다.
	 * @param filepath 파일 경로
	 * @return 파일 확장자
	 */
	public static final String ext(String filepath) {
		int index = filepath.lastIndexOf(".");
		return index < 0 ? "" : filepath.substring(index + 1);
	}
	/**주어진 파일 경로에서 디렉토리 경로를 추출하여 반환한다.
	 * @param filepath		파일 경로
	 * @param separator 파일 구분자. 공백일 경오 OS의 파일 구분자를 사용한다.
	 * @return directory name
	 */
	public static final String dirName(String filepath, String separator) {
		int index = filepath.lastIndexOf(ifEmpty(separator, File.separator));
		return index < 0 ? "" : filepath.substring(0, index);
	}
	
	public static final long size(MultipartFile... files) {
		long result = 0;
		if (!isEmpty(files))
			for (MultipartFile file: files)
				result += file.getSize();
		return result;
	}

	private boolean isValid(File file) {
		return file != null && file.exists();
	}
	/**디렉토리를 생성한다.
	 * @param dir 디렉토리 경로
	 * @return
	 * <ul><li>디렉토리가 이미 존재하거나 생성했으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean create(String dir) {
		if (isEmpty(dir)) return false;
		File file = new File(dir);
		boolean created = file.exists() ? true : file.mkdirs();
		if (!created)
			log().debug(() -> "Failed to create " + dir);
		return created;
	}
	/**주어진 경로로 파일을 생성하고 data를 저장한다. 필요할 경우 경로 상의 디렉토리도 생성한다.
	 * @param path	파일 경로
	 * @param data 파일 데이터
	 * @param separator 파일 구분자. 공백일 경우 OS의 파일 구분자를 사용한다.
	 */
	public void create(String path, byte[] data, String separator) {
		create(dirName(path, separator));
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(path));
			out.write(data);
		} catch (IOException e) {
			throw runtimeException(e);
		} finally {
			if (out != null)
			try {
				out.flush();
				out.close();
			} catch (Exception ex) {
				throw runtimeException(ex);
			}
		}
	}
	/**내부적으로 {@link #create(String, byte[], String) create(path, data, null)}를 호출한다.
	 */
	public void create(String path, byte[] data) {
		create(path, data, null);
	}
	/**oldPath의 파일을 newPath로 경로와 이름을 변경한다.
	 * @param oldPath 변경할 파일의 경로와 이름
	 * @param newPath 파일의 새 경로와 이름
	 * @return
	 * <ul><li>원래 파일이 실제로 새 경로로 이름이 변경됐으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean rename(String oldPath, String newPath) {
		boolean renamed = rename(new File(oldPath), newPath);
		if (!renamed)
			log().error(() -> "Failed to rename " + oldPath + " to " + newPath);
		return renamed;
	}
	/**file의 이름을 newPath로 지정한 경로로 변경한다.
	 * @param file 변경한 파일
	 * @param newPath 새 경로
	 * @return
	 * <ul><li>파일이 실제로 주어진 경로로 이름이 변경됐으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean rename(File file, String newPath) {
		return isValid(file) && file.renameTo(new File(newPath));
	}
	/**주어진 경로의 파일들을 삭제한다.
	 * @param paths 파일 경로
	 * @return
	 * <ul><li>지정한 파일이 모두 삭제됐으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean delete(String... paths) {
		boolean result = true;
		for (String path: paths) {
			boolean deleted = delete(new File(path));
			if (!deleted) {
				log().debug(() -> "Failed to delete " + path + ".");
				result = false;
			}
		}
		return result;
	}
	/**file을 삭제한다.
	 * @param file 파일
	 * @return
	 * <ul><li>지정한 파일과 해당 파일의 모든 하위 파일들이 삭제됐으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean delete(File file) {
		if (!isValid(file)) return false;

		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children != null)
				for (File child: children)
					if (!delete(child)) return false;
		}
		return file.delete();
	}
	/**파일을 지정한 경로로 복사한다. 원본 파일이 디렉토리일 경우 모든 하위 디렉토리도 복사한다.
	 * @param src	원본 파일 경로
	 * @param dest	대상 경로
	 */
	public void copy(String src, String dest) {
		if (!src.equals(dest))
			copy(new File(src), dest);
	}
	/**file을 지정한 경로로 복사한다. file이 디렉토리일 경우 모든 하위 디렉토리도 복사한다.
	 * @param src	원본 파일
	 * @param dest	대상 경로
	 */
	public void copy(File src, String dest) {
		if (!isValid(src)) return;

		if (src.isFile()) {
			FileInputStream in = null;
			FileOutputStream out = null;
			try {
				create(dirName(dest, null));
				in = new FileInputStream(src);
				out = new FileOutputStream(new File(dest));
				byte[] bytes = new byte[1024];
				int read = 0;
				while ((read = in.read(bytes)) != -1)
					out.write(bytes, 0, read);
			} catch (Exception e) {
				throw runtimeException(e);
			} finally {
				try {
					if (in != null)
						in.close();
					if (out != null)
						out.close();
				} catch (Exception ex) {
					throw runtimeException(ex);
				}
			}
		}
		if (src.isDirectory()) {
			create(dest);
			File[] children = src.listFiles();
			if (children != null) {
				for (File child: children)
					copy(child, dest + File.separator + child.getName());
			}
		}
	}
	/**src 경로의 파일을 dest 경로로 이동한다.
	 * @param src	원본 파일 경로
	 * @param dest	이동 경로
	 * @return
	 * <ul><li>원본 파일이 이동됐으면 true</li>
	 * 	   <li>그렇지 않으면 false</li>
	 * </ul>
	 */
	public boolean move(String src, String dest) {
		boolean moved = src.equals(dest) ? false : move(new File(src), dest);
		if (!moved)
			log().error(() -> "Failed to move " + src + " to " + dest);
		return moved;
	}
	/**file을 지정한 경로로 이동한다.
	 * @param src	원본 파일
	 * @param dest	이동 경로
	 * @return
	 * <ul><li>true if the source file is actually moved</li>
	 * 	   <li>false otherwise</li>
	 * </ul>
	 */
	public boolean move(File src, String dest) {
		create(dest);
		return rename(src, dest + File.separator + src.getName());
	}
	/**input에서 데이터를 읽어 바이트 배열로 반환한다.
	 * @param input InputStream
	 * @return 바이트 배열
	 */
	public byte[] read(InputStream input) {
		if (input == null) return null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			write(input, out);
			byte[] result = out.toByteArray();
			out.close();
			return result;
		} catch (Exception e) {
			throw runtimeException(e);
		}
	}
	/**input의 데이터를 읽어 out로 저장한다.
	 * @param input InputStream
	 * @param out	OutputStream
	 */
	public void write(InputStream input, OutputStream out) {
		if (input == null || out == null) return;
		try {
			int read = 0;
			byte[] data = new byte[1024 * 4];
			while ((read = input.read(data, 0, data.length)) != -1)
				out.write(data, 0, read);
			out.flush();
		} catch (Exception e) {
			throw runtimeException(e);
		}
	}
	/**업로드 파일 처리를 돕기위한 유틸리티 클래스
	 */
	public static class Upload implements Serializable {
		private static final long serialVersionUID = 1L;

		private String
			name,
			filename,
			contentType;
		private byte[] data;
		/**새 Upload를 생성한다.*/
		public Upload() {}
		/**새 Upload를 생성한다.
		 * @param name 프로그램에서 식별하기 위한 Upload 이름
		 * @param path 파일 경로
		 * @param data 파일의 데이터
		 */
		public Upload(String name, String path, byte[] data) {
			setName(name).setFilename(path).setData(data);
		}
		/**Upload가 올바른 지 반환한다.<br />
		 * 올바른 Upload는 파일 이름과 데이터를 가져야 한다.
		 * @return
		 * <ul><li>Upload가 올바르면 true</li>
		 * 	   <li>false otherwise</li>
		 * </ul>
		 */
		public boolean isValid() {
			return !isEmpty(filename) && data != null;
		}
		/**프로그램에서 Upload를 식별하는 이름
		 * @return Upload 이름
		 */
		public String name() {
			return name;
		}
		/**프로그램에서 Upload를 식별하는 이름을 설정한다.
		 * @param name 프로그램에서 Upload를 식별하는 이름
		 * @return Upload 자신
		 */
		public Upload setName(String name) {
			this.name = name;
			return this;
		}
		/**업로드한 파일의 경로 중 디렉토리 경로를 제외한 이름을 반환한다.
		 * @return 파일 이름
		 */
		public String filename() {
			return filename;
		}
		/**업로드한 파일의 경로 중 디렉토리 경로를 제외한 이름을 설정한다.
		 * @param path 파일 경로
		 * @return Upload 자신
		 */
		public Upload setFilename(String path) {
			this.filename = isEmpty(path) ? path: FileSupport.fileName(path.replace("\\", "/"), "/");
			return this;
		}
		/**업로드한 파일의 content type을 반환한다.
		 * @return 업로드한 파일의 content type
		 */
		public String contentType() {
			return contentType;
		}
		/**업로드한 파일의 content type을 설정한다.
		 * @param contentType 업로드한 파일의 content type
		 * @return Upload 자신
		 */
		public Upload setContentType(String contentType) {
			this.contentType = contentType;
			return this;
		}
		/**업로드한 파일의 데이터를 반환한다.
		 * @return 파일 데이터
		 */
		public byte[] data() {
			return data;
		}
		/**업로드한 파일의 데이터를 설정한다.
		 * @param data 파일 데이터
		 * @return the Upload
		 */
		public Upload setData(byte[] data) {
			this.data = data;
			return this;
		}
		/**업로드한 파일을 지정한 path로 저장한다.
		 * @param path 저장 경로
		 */
		public void write(String path) {
			new FileSupport().create(path, data);
		}
	}
}