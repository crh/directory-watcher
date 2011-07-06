package org.escidoc.watcher.domain.internal;

import org.escidoc.watcher.domain.FileEvent;
import org.escidoc.watcher.domain.Subscriber;

public class SubscriberImpl implements Subscriber {

  @Override
  public void consume(FileEvent event) {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

}
