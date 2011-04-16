package org.escidoc.watcher;

import org.escidoc.watcher.domain.FileEvent;

public interface Consumer {

    void consume(FileEvent event);

}
