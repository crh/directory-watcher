package org.escidoc.watcher.domain;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;

import org.joda.time.DateTime;

public interface FileEvent {

    Kind<?> getKind();

    Path getFullPath();

    FileType getFileType();

    DateTime firedOn();
}