package org.escidoc.watcher;

public class AppBuilder {

  public static Ebox build(String... args) {
    return new EboxImpl(args);
  }

}
