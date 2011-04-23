package org.escidoc.watcher.domain.internal;

import org.escidoc.watcher.domain.Consumer;
import org.escidoc.watcher.domain.FileEvent;

import com.google.common.util.concurrent.UncheckedTimeoutException;

import de.escidoc.core.client.IngestHandlerClient;
import de.escidoc.core.client.interfaces.IngestHandlerClientInterface;

public class ItemIngester implements Consumer {

    IngestHandlerClientInterface client = new IngestHandlerClient();

    @Override
    public void consume(final FileEvent event) {
        throw new UncheckedTimeoutException("Ingest not yet implemented");
    }

}
