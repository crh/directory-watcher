package org.escidoc.watcher.domain;

import java.nio.file.StandardWatchEventKind;

import org.escidoc.watcher.domain.internal.ItemCreator;

public class FileUploader implements Subscriber {
    private final Consumer consumer = new ItemCreator();

    // private final Consumer consumer = new ItemIngester();

    @Override
    public void consume(final FileEvent event) {
        if (event.getKind().equals(StandardWatchEventKind.ENTRY_CREATE)) {
            consumer.consume(event);
        }
    }

}
