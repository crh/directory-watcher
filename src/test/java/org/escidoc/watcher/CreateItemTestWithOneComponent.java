package org.escidoc.watcher;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.om.item.ItemProperties;
import de.escidoc.core.resources.om.item.StorageType;
import de.escidoc.core.resources.om.item.component.Component;
import de.escidoc.core.resources.om.item.component.ComponentContent;
import de.escidoc.core.resources.om.item.component.ComponentProperties;
import de.escidoc.core.resources.om.item.component.Components;

public class CreateItemTestWithOneComponent {
    private static final Logger LOG = LoggerFactory
        .getLogger(CreateItemTestWithOneComponent.class);

    private static final String TEST_FILE_PATH = "/hello.txt";

    private StagingHandlerClientInterface stagingClient;

    private ItemHandlerClientInterface itemClient;

    private Authentication auth;

    private final Components componentList = new Components();

    private final Component component = new Component();

    private final ComponentContent content = new ComponentContent();

    private final ComponentProperties componentProperties =
        new ComponentProperties();

    @Before
    public void init() throws Exception {
        authentificate();
        initStagingClient();
        initItemClient();
    }

    private void initStagingClient() throws EscidocException,
        InternalClientException, TransportException {
        stagingClient = new StagingHandlerClient(auth.getServiceAddress());
        stagingClient.setTransport(TransportProtocol.REST);
        stagingClient.setHandle(auth.getHandle());
    }

    @After
    public void post() throws Exception {
        if (auth != null)
            auth.logout();
    }

    @Test
    public void shouldReturnNonEmptyContentXlinkHrefWhenItemIsRetrieved()
        throws Exception {

        final Item createdItem = whenItemWithAComponentIsCreated();
        then(createdItem);
    }

    private void then(final Item createdItem) throws EscidocException,
        InternalClientException, TransportException {
        final String objid = createdItem.getObjid();
        LOG.info("Object ID: " + objid);

        final Components components = findAllcomponents(objid);
        assertTrue("component size should be bigger than 0",
            components.size() > 0);

        for (final Component component : components) {
            final ComponentContent content = component.getContent();
            final String xLinkHref = content.getXLinkHref();

            assertNotNull("XLink HREF should not be null", xLinkHref);
            assertTrue("XLink HREF should not be empty", xLinkHref.length() > 0);
        }
    }

    private Item whenItemWithAComponentIsCreated() throws EscidocException,
        InternalClientException, TransportException,
        ParserConfigurationException {
        final InputStream stream = loadFile();
        final URL contentRef = putFileInStagingServer(stream);
        final Item createdItem = createItemWithComponent(contentRef);
        return createdItem;
    }

    private Components findAllcomponents(final String objid)
        throws EscidocException, InternalClientException, TransportException {
        return itemClient.retrieve(objid).getComponents();
    }

    private Item createItemWithComponent(final URL contentRef)
        throws ParserConfigurationException, EscidocException,
        InternalClientException, TransportException {
        final Item item = createNewItemUsing(contentRef);
        final Item createdItem = itemClient.create(item);
        return createdItem;
    }

    private URL putFileInStagingServer(final InputStream stream)
        throws EscidocException, InternalClientException, TransportException {
        final URL contentRef = stagingClient.upload(stream);
        LOG.debug("The file can be found in: " + contentRef);
        return contentRef;
    }

    private InputStream loadFile() {
        final InputStream stream =
            getClass().getResourceAsStream(TEST_FILE_PATH);
        assertNotNull("Stream is null.", stream);
        return stream;
    }

    private void authentificate() throws EscidocClientException {
        auth =
            new Authentication(AppConstant.SERVICE_ADDRESS,
                AppConstant.SYSADMIN_LOGIN_NAME, AppConstant.SYSADMIN_PASSWORD);
    }

    private void initItemClient() throws InternalClientException {
        itemClient = new ItemHandlerClient(auth.getServiceAddress());
        itemClient.setHandle(auth.getHandle());
        itemClient.setTransport(TransportProtocol.REST);
    }

    private Item createNewItemUsing(final URL contentRef)
        throws ParserConfigurationException {
        final Item item = new Item();
        final ItemProperties properties = item.getProperties();
        properties.setContext(new ContextRef(AppConstant.CONTEXT_ID));
        properties.setContentModel(new ContentModelRef(
            AppConstant.CONTENT_MODEL_ID));
        setMdRecords(item);
        setComponents(item, contentRef);
        return item;
    }

    private void setMdRecords(final Item item)
        throws ParserConfigurationException {
        final MetadataRecord mdRecord = new MetadataRecord();
        mdRecord.setName("escidoc");

        final DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document = builder.newDocument();
        final Element element = document.createElementNS(null, "myMdRecord");
        mdRecord.setContent(element);

        final MetadataRecords mdRecords = new MetadataRecords();
        mdRecords.add(mdRecord);
        item.setMetadataRecords(mdRecords);
    }

    private void setComponents(final Item item, final URL contentRef) {
        setComponentProperties(component, contentRef);
        setComponentContent(component, contentRef);
        componentList.add(component);
        item.setComponents(componentList);
    }

    private void setComponentContent(
        final Component component, final URL contentRef) {
        content.setXLinkHref(contentRef.toString());
        content.setStorage(StorageType.INTERNAL_MANAGED);
        component.setContent(content);
    }

    private void setComponentProperties(
        final Component component, final URL contentRef) {
        componentProperties.setDescription("Random content");
        componentProperties.setFileName(contentRef.getFile());
        componentProperties.setVisibility("public");
        componentProperties.setContentCategory("pre-print");
        component.setProperties(componentProperties);
    }
}