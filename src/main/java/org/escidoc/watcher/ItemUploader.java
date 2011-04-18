package org.escidoc.watcher;

import org.escidoc.watcher.domain.FileEvent;
import org.escidoc.watcher.domain.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.item.Item;

public class ItemUploader implements Consumer {
    private static final Logger LOG = LoggerFactory
        .getLogger(ItemUploader.class);

    private final ItemHandlerClientInterface client = new ItemHandlerClient(
        AppConstant.SERVICE_ADDRESS);

    public ItemUploader() {
        client.setTransport(TransportProtocol.REST);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void consume(final FileEvent event) {
        if (event.getFileType().equals(FileType.FILE)) {
            try {
                client.login(AppConstant.SERVICE_ADDRESS,
                    AppConstant.SYSADMIN_LOGIN_NAME,
                    AppConstant.SYSADMIN_PASSWORD);

                final Item created =
                    client.create(new ItemBuilder.Builder(new ContextRef(
                        "escidoc:10281"), new ContentModelRef("escidoc:3001"))
                        .build());
                LOG.debug(created.getObjid());
            }
            catch (final EscidocClientException e) {
                LOG.error("error: ", e);
            }
        }
    }
}
