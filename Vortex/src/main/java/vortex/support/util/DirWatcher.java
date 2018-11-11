package vortex.support.util;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import vortex.support.AbstractComponent;

public class DirWatcher extends AbstractComponent {
	private static DirWatcher inst;
	
	public static DirWatcher get() {
		return inst != null ? inst : (inst = new DirWatcher());
	}
	
	private boolean started;
	private HashSet<String> dirs = new HashSet<>();
	private HashMap<Path, WatchEvent.Kind<?>> pathEvents = new HashMap<>();
	private WatchService wsvc;
	
	private static final Consumer<Path> ignore = path -> {};
	private Consumer<Path>
		fileCreated = ignore,
		fileModified = ignore,
		fileDeleted = ignore;
	
	public static String dirPath(String path) {
		if (path == null)
			return null;
		
		int last = path.lastIndexOf("/");
		if (last < 0)
			last = path.lastIndexOf(File.separator);
		return path.substring(0, last);
	}
	
	public Set<String> getDirs() {
		return dirs != null ? dirs : Collections.emptySet();
	}
	
	public DirWatcher add(String path) {
		if (path != null && !path.trim().isEmpty()) {
			path = path.trim();
			if (!path.isEmpty())
				dirs.add(path);
		}
		return this;
	}
	
	public DirWatcher add(String... paths) {
		if (paths != null) {
			for (String path: paths)
				add(path);
		}
		return this;
	}
	
	public DirWatcher onFileCreate(Consumer<Path> handler) {
		fileCreated = handler;
		return this;
	}
	
	public DirWatcher onFileChange(Consumer<Path> handler) {
		fileModified = handler;
		return this;
	}
	
	public DirWatcher onFileDelete(Consumer<Path> handler) {
		fileDeleted = handler;
		return this;
	}
	
	public void start() {
		if (!started && dirs != null & !dirs.isEmpty())
			try {
				started = true;
				wsvc = FileSystems.getDefault().newWatchService();
				dirs.forEach(this::register);
				new Thread(this::watch).start();
			} catch (Exception e) {
				started = false;
				throw new RuntimeException(e);
			}
	}
	
	private void watch() {
		while (true) {
			WatchKey key;
			try {
				key = wsvc.take();
				if (key == null) continue;
				
				List<WatchEvent<?>> evts = key.pollEvents();
				for (WatchEvent<?> evt: evts) {
					Path path = ((Path)evt.context()).toAbsolutePath();
					WatchEvent.Kind<?> kind = evt.kind();
//					log().debug(() -> path + ": " +  kind);
					
					WatchEvent.Kind<?> stored = pathEvents.get(path);
					if (stored == null)
						pathEvents.put(path, kind);
					else {
						pathEvents.clear();
//						pathEvents.remove(path);
						fire(!kind.equals(StandardWatchEventKinds.ENTRY_DELETE) ? stored : kind, path);
					}
				}
				key.reset();
			} catch (Exception e) {
				pathEvents.clear();
				throw new RuntimeException(e);
			}
			
		}
	}
	
	private void fire(WatchEvent.Kind<?> kind, Path path) {
		if (StandardWatchEventKinds.ENTRY_CREATE.equals(kind)) {
			fileCreated.accept(path);
		} else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(kind)) {
			fileModified.accept(path);
		} else if (StandardWatchEventKinds.ENTRY_DELETE.equals(kind)) {
			fileDeleted.accept(path);
		}
	}
	
	private void register(String dir) {
		try {
			Path path = Paths.get(dir);
			path.register(
				wsvc,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_MODIFY,
				StandardWatchEventKinds.ENTRY_DELETE
			);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		String[] paths = {
			"D:\\GitHub\\Vortex Project\\Vortex\\src\\test\\resources\\properties"
		};
		DirWatcher.get()
			.add(paths)
			.onFileCreate(path -> System.out.println(path + " created."))
			.onFileChange(path -> System.out.println(path + " modified."))
			.onFileDelete(path -> System.out.println(path + " deleted."))
			.start();
	}

}