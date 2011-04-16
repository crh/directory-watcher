package org.escidoc.watcher;

import org.escidoc.watcher.domain.FileEvent;

public interface Uploader {

    void upload(FileEvent event);

}
