package org.escidoc.watcher;

import java.io.File;

public class DirectoryImpl implements Directory {

    private final File file;

    public DirectoryImpl(File file) {
	if (notDirectory(file)) {
	    throw new RuntimeException(file.toString() + "is not a directory");
	}
	this.file = file;
    }

    private boolean notDirectory(File file) {
	return !file.isDirectory();
    }

    public File getFile() {
	return file;
    }

}
