package org.escidoc.watcher.domain;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;

public interface FileEvent {

    public abstract Kind<?> getKind();

    public abstract Path getFullPath();

}