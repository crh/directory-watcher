package org.escidoc.watcher;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;

public class FileEventImpl implements FileEvent {

    private final Kind<?> kind;

    private final Path resolved;

    public FileEventImpl(Kind<?> kind, Path resolve) {
        this.kind = kind;
        this.resolved = resolve;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.escidoc.watcher.FileEvent#getKind()
     */
    public Kind<?> getKind() {
        return kind;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.escidoc.watcher.FileEvent#getFullPath()
     */
    public Path getFullPath() {
        return resolved;
    }

}
