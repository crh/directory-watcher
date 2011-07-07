package org.escidoc.watcher.domain;

public interface Subscriber {

  void consume(FileEvent event);

}