package org.escidoc.watcher;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Ebox {

  void start() throws FileNotFoundException, IOException, InterruptedException;

}
