package org.escidoc.watcher;

import org.escidoc.watcher.domain.FileEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.UncheckedTimeoutException;

import de.escidoc.core.client.IngestHandlerClient;
import de.escidoc.core.client.interfaces.IngestHandlerInterface;

public class ItemIngester implements Consumer {
    private static final Logger LOG = LoggerFactory
        .getLogger(ItemIngester.class);

    IngestHandlerInterface client = new IngestHandlerClient();

    @Override
    public void consume(final FileEvent event) {
        throw new UncheckedTimeoutException("Ingest not yet implemented");
    }

}
