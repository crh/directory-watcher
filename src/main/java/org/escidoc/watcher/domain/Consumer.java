package org.escidoc.watcher.domain;

public interface Consumer {

  void consume(FileEvent event);

}
