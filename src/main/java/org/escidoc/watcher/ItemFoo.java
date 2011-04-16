package org.escidoc.watcher;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Preconditions;

import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.om.item.ItemProperties;

public class ItemFoo {

    public static class Builder {

        private final Item item = new Item();

        private final MetadataRecords metadataRecords = new MetadataRecords();

        private final MetadataRecord mdRecord = new MetadataRecord();

        private final ItemProperties properties = new ItemProperties();

        private static final String ESCIDOC = "escidoc";

        private final ContextRef contextRef;

        private final ContentModelRef contentModelRef;

        public Builder(final ContextRef contextRef,
            final ContentModelRef contentModelRef) {
            Preconditions.checkNotNull(contextRef, "contextRef is null: %s",
                contextRef);
            Preconditions.checkNotNull(contentModelRef,
                "contentModelRef is null: %s", contentModelRef);
            this.contextRef = contextRef;
            this.contentModelRef = contentModelRef;
        }

        public Item build() {
            final DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                builder = factory.newDocumentBuilder();
                final Document doc = builder.newDocument();
                final MetadataRecord mdRecord = new MetadataRecord();

                mdRecord.setName(ESCIDOC);
                final Element element = doc.createElementNS(null, "myMdRecord");
                mdRecord.setContent(element);

                metadataRecords.add(mdRecord);
                item.setMetadataRecords(metadataRecords);

            }
            catch (final ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            properties.setContext(contextRef);
            properties.setContentModel(contentModelRef);

            item.setProperties(properties);
            return item;
        }
    }
}