package org.escidoc.watcher.domain.internal;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;

import org.escidoc.watcher.domain.FileEvent;

public class FileEventImpl implements FileEvent {

    private final Kind<?> kind;

    private final Path resolved;

    public FileEventImpl(WatchEvent<?> watchEvent, Path monitoredPath) {
	this.kind = watchEvent.kind();
	this.resolved = monitoredPath.resolve((Path) watchEvent.context());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.escidoc.watcher.FileEvent#getKind()
     */
    @Override
    public Kind<?> getKind() {
	return kind;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.escidoc.watcher.FileEvent#getFullPath()
     */
    @Override
    public Path getFullPath() {
	return resolved;
    }

    @Override
    public String toString() {
	return "FileEventImpl [getKind()=" + getKind() + ", getFullPath()="
		+ getFullPath() + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((kind == null) ? 0 : kind.hashCode());
	result = prime * result
		+ ((resolved == null) ? 0 : resolved.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	FileEventImpl other = (FileEventImpl) obj;
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
}
