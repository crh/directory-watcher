package org.escidoc.watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKind;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private static final String TO_MONITOR = "./sync-me";

    private static Path path = Paths.get(TO_MONITOR);

    public static void main(final String[] args) throws Exception {
        final WatchService service = init();
        for (;;) {
            usingPull(service);
        }
    }

    private static WatchService init() throws IOException {
        final WatchService service = FileSystems.getDefault().newWatchService();
        path.register(service, StandardWatchEventKind.ENTRY_CREATE,
            StandardWatchEventKind.ENTRY_DELETE,
            StandardWatchEventKind.ENTRY_MODIFY,
            StandardWatchEventKind.OVERFLOW);
        return service;
    }

    private static void usingPull(final WatchService service)
        throws InterruptedException {
        extract(service.take());
    }

    private static void extract(final WatchKey key) {
        checkForValidity(key);
        key.reset();
    }

    private static void checkForValidity(final WatchKey key) {
        if (isInvalid(key)) {
            LOG.warn("Invalid key: " + key);
        }
        else {
            processEvent(key);
        }
    }

    private static void processEvent(final WatchKey key) {
        for (final WatchEvent<?> watchEvent : key.pollEvents()) {
            if (watchEvent.kind() instanceof Path) {
                final String name = watchEvent.kind().name();
                final Class<?> type = watchEvent.kind().type();
                final Path context = (Path) watchEvent.context();
                LOG.info("event kind: " + name + " context: "
                    + path.resolve(context));
            }
        }
    }

    private static boolean isInvalid(final WatchKey key) {
        return !key.isValid();
    }
}
