package org.escidoc.watcher;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKind;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

/**
 * Hello world!
 * 
 */
public class App {

    private static final String TO_MONITOR = "./sync-me";
    private static Path path = Paths.get(TO_MONITOR);

    public static void main(String[] args) throws Exception {
	WatchService service = FileSystems.getDefault().newWatchService();

	path.register(service, StandardWatchEventKind.ENTRY_CREATE,
		StandardWatchEventKind.ENTRY_DELETE,
		StandardWatchEventKind.ENTRY_MODIFY,
		StandardWatchEventKind.OVERFLOW);
	while (true) {
	    usingPull(service);
	}
    }

    private static void usingPull(WatchService service)
	    throws InterruptedException {
	extract(service.take());
    }

    private static void extract(WatchKey key) {
	if (key.isValid()) {
	    List<WatchEvent<?>> eventList = key.pollEvents();
	    for (WatchEvent<?> watchEvent : eventList) {
		@SuppressWarnings("unchecked")
		Kind<Path> kind = (Kind<Path>) watchEvent.kind();
		String name = kind.name();
		Path context = (Path) watchEvent.context();
		System.out.println("event kind: " + name + " context: "
			+ path.resolve(context));
	    }
	} else {
	    System.err.println("invalid: " + key);
	}
	key.reset();
    }
}
