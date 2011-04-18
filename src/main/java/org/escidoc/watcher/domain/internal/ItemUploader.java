package org.escidoc.watcher.domain.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.escidoc.watcher.AppConstant;
import org.escidoc.watcher.domain.Consumer;
import org.escidoc.watcher.domain.FileEvent;
import org.escidoc.watcher.domain.FileType;
import org.escidoc.watcher.domain.ItemBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.StagingHandlerClient;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.client.interfaces.StagingHandlerClientInterface;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.item.Item;

public class ItemUploader implements Consumer {

    private static final Logger LOG = LoggerFactory
        .getLogger(ItemUploader.class);

    private final ItemHandlerClientInterface itemClient =
        new ItemHandlerClient(AppConstant.SERVICE_ADDRESS);

    private final StagingHandlerClientInterface stagingClient =
        new StagingHandlerClient(AppConstant.SERVICE_ADDRESS);

    public ItemUploader() {
        itemClient.setTransport(TransportProtocol.REST);
        stagingClient.setTransport(TransportProtocol.REST);
    }

    @Override
    public void consume(final FileEvent event) {
        if (event.getFileType().equals(FileType.FILE)) {
            tryCreateItem(event);
        }
    }

    private void tryCreateItem(final FileEvent event) {
        try {
            login();
            final InputStream stream = loadFile(event);
            final URL contentRef = putFileInStagingServer(stream);
            final Item created = createItem(contentRef);
            relesaseIt(created);
            LOG.debug(created.getObjid());
        }
        catch (final EscidocClientException e) {
            LOG.error("Fail to create item with component. " + e);
        }
        catch (final IOException e) {
            LOG.error("Fail to read file. " + e);
        }

    }

    private void relesaseIt(final Item created) throws EscidocClientException {
        final TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(created.getLastModificationDate());
        final Result submit = itemClient.submit(created, taskParam);
        taskParam.setLastModificationDate(submit.getLastModificationDate());

        itemClient.release(created, taskParam);

    }

    private URL putFileInStagingServer(final InputStream stream)
        throws EscidocClientException {
        final URL contentRef = stagingClient.upload(stream);
        LOG.debug("The file can be found in: " + contentRef);
        return contentRef;
    }

    private InputStream loadFile(final FileEvent event) throws IOException {
        return Files.newInputStream(event.getFullPath(),
            StandardOpenOption.READ);

    }

    private Item createItem(final URL contentUrl) throws EscidocException,
        InternalClientException, TransportException {
        final Item created =
            itemClient.create(new ItemBuilder.Builder(new ContextRef(
                AppConstant.CONTEXT_ID), new ContentModelRef(
                AppConstant.CONTENT_MODEL_ID)).withContent(contentUrl).build());
        return created;
    }

    private void login() throws EscidocClientException {
        final Authentication authentication =
            new Authentication(AppConstant.SERVICE_ADDRESS,
                AppConstant.SYSADMIN_LOGIN_NAME, AppConstant.SYSADMIN_PASSWORD);
        final String handle = authentication.getHandle();
        itemClient.setHandle(handle);
        stagingClient.setHandle(handle);
    }

}
