package org.escidoc.watcher.domain;

public interface Uploader {

  void upload(FileEvent event);

}
