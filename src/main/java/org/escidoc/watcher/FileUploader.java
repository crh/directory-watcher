package org.escidoc.watcher;

import java.nio.file.StandardWatchEventKind;

import org.escidoc.watcher.domain.FileEvent;
import org.escidoc.watcher.domain.Subscriber;

public class FileUploader implements Subscriber {
    // private final Consumer consumer = new ItemUploader();

    private final Consumer consumer = new ItemIngester();

    @Override
    public void consume(final FileEvent event) {
        if (event.getKind().equals(StandardWatchEventKind.ENTRY_CREATE)) {
            consumer.consume(event);
        }
    }

}
