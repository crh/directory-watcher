package org.escidoc.watcher.domain.internal;

import org.escidoc.watcher.domain.FileEvent;
import org.escidoc.watcher.domain.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StdLogger implements Subscriber {
    private static final Logger LOG = LoggerFactory.getLogger(StdLogger.class);

    @Override
    public void consume(final FileEvent event) {
        LOG.info("got: " + event);
    }

}
