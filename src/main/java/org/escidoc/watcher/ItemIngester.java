package org.escidoc.watcher;

import org.escidoc.watcher.domain.FileEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.core.client.IngestHandlerClient;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.interfaces.IngestHandlerInterface;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.item.Item;

public class ItemIngester implements Consumer {
    private static final Logger LOG = LoggerFactory
        .getLogger(ItemIngester.class);

    IngestHandlerInterface client = new IngestHandlerClient();

    @Override
    public void consume(final FileEvent event) {
        client = new IngestHandlerClient();
        client.setTransport(TransportProtocol.REST);
        client.login(AppConstant.SERVICE_ADDRESS,
            AppConstant.SYSADMIN_LOGIN_NAME, AppConstant.SYSADMIN_PASSWORD);

        final Item created =
            client.create(new ItemFoo.Builder(new ContextRef("escidoc:10281"),
                new ContentModelRef("escidoc:3001")).build());
        LOG.debug(created.getObjid());
    }

}
