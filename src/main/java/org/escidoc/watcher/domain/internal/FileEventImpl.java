package org.escidoc.watcher.domain.internal;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;

import org.escidoc.watcher.domain.FileEvent;
import org.escidoc.watcher.domain.FileType;
import org.joda.time.DateTime;

import com.google.common.base.Preconditions;

public class FileEventImpl implements FileEvent {

  private final Kind<?> kind;

  private final Path resolved;

  private final Path context;

  public FileEventImpl(final WatchEvent<?> watchEvent, final Path monitoredPath) {
    Preconditions.checkNotNull(watchEvent, "watchEvent is null: %s", watchEvent);
    Preconditions.checkNotNull(monitoredPath, "monitoredPath is null: %s",
        monitoredPath);
    this.kind = watchEvent.kind();
    context = (Path) watchEvent.context();
    this.resolved = monitoredPath.resolve(context);
  }

  @Override
  public Kind<?> getKind() {
    return kind;
  }

  @Override
  public Path getFullPath() {
    return resolved;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((kind == null) ? 0 : kind.hashCode());
    result = prime * result + ((resolved == null) ? 0 : resolved.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final FileEventImpl other = (FileEventImpl) obj;
    if (kind == null) {
      if (other.kind != null)
        return false;
    } else if (!kind.equals(other.kind))
      return false;
    if (resolved == null) {
      if (other.resolved != null)
        return false;
    } else if (!resolved.equals(other.resolved))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FileEventImpl [getKind()=" + getKind() + ", getFullPath()="
        + getFullPath() + ", hashCode()=" + hashCode() + ", getFileType()="
        + getFileType() + ", firedOn()=" + firedOn() + "]";
  }

  @Override
  public FileType getFileType() {
    if (resolved.toFile().isDirectory()) {
      return FileType.DIRECTORY;
    }
    return FileType.FILE;
  }

  @Override
  public DateTime firedOn() {
    return new DateTime();
  }

  @Override
  public Path getName() {
    return context.getFileName();
  }
}
