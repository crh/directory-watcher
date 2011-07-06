package org.escidoc.watcher.domain;

import java.nio.file.StandardWatchEventKinds;

import org.escidoc.watcher.domain.internal.AppConfig;
import org.escidoc.watcher.domain.internal.ItemCreator;

import com.google.common.base.Preconditions;

public class FileUploader implements Subscriber {

    private final Consumer consumer;

    public FileUploader(AppConfig config) {
        Preconditions.checkNotNull(config, "config is null.");
        consumer = new ItemCreator(config);
    }

    @Override
    public void consume(final FileEvent event) {
        if (event.getKind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            consumer.consume(event);
        }
    }

}
