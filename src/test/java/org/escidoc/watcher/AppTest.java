package org.escidoc.watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKind;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.escidoc.watcher.domain.FileEvent;
import org.escidoc.watcher.domain.internal.FileEventImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AppTest extends TestCase {

    private static final Logger LOG = LoggerFactory.getLogger(AppTest.class);

    private static final String TO_MONITOR =
        "/home/chh/projects/personal/directory-watcher/sync-me";

    @Test
    public void testApp() throws IOException, InterruptedException {
        WatchService service = FileSystems.getDefault().newWatchService();

        Path path = Paths.get(TO_MONITOR);
        path.register(service, StandardWatchEventKind.ENTRY_CREATE,
            StandardWatchEventKind.ENTRY_DELETE,
            StandardWatchEventKind.ENTRY_MODIFY,
            StandardWatchEventKind.OVERFLOW);
        for (;;) {
            WatchKey key = service.take();
            if (key.isValid()) {
                List<WatchEvent<?>> eventList = key.pollEvents();
                for (WatchEvent<?> watchEvent : eventList) {
                    Kind<?> kind = watchEvent.kind();
                    String name = kind.name();
                    if (watchEvent.context() instanceof Path) {
                        Path context = (Path) watchEvent.context();
                        LOG.debug("event kind: " + name + " context: "
                            + path.resolve(context));

                        fireEvent(new FileEventImpl(watchEvent, path));
                    }
                }
                if (!key.reset()) {
                    LOG.warn("can not reset key.");
                }
            }
            else {
                LOG.warn("invalid: " + key);
            }
        }
    }

    private final List<FileEvent> list = new ArrayList<FileEvent>();

    private void fireEvent(FileEventImpl fileEvent) {
        list.add(fileEvent);
    }

}