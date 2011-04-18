package org.escidoc.watcher.domain;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.watcher.AppConstant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Preconditions;

import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.om.item.ItemProperties;
import de.escidoc.core.resources.om.item.component.Component;
import de.escidoc.core.resources.om.item.component.ComponentContent;
import de.escidoc.core.resources.om.item.component.ComponentProperties;
import de.escidoc.core.resources.om.item.component.Components;

public class ItemBuilder {

    public static class Builder {

        private final Item item = new Item();

        private final MetadataRecords metadataRecords = new MetadataRecords();

        private final MetadataRecord mdRecord = new MetadataRecord();

        private final ItemProperties properties = new ItemProperties();

        private final ContextRef contextRef;

        private final ContentModelRef contentModelRef;

        private URL contentUrl;

        public Builder(final ContextRef contextRef,
            final ContentModelRef contentModelRef) {
            Preconditions.checkNotNull(contextRef, "contextRef is null: %s",
                contextRef);
            Preconditions.checkNotNull(contentModelRef,
                "contentModelRef is null: %s", contentModelRef);
            this.contextRef = contextRef;
            this.contentModelRef = contentModelRef;
        }

        public Builder withContent(final URL contentUrl) {
            Preconditions.checkNotNull(contentUrl, "contentUrl is null: %s",
                contentUrl);
            this.contentUrl = contentUrl;
            return this;
        }

        public Item build() {
            final DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                builder = factory.newDocumentBuilder();
                final Document doc = builder.newDocument();

                mdRecord.setName(AppConstant.ESCIDOC);
                final Element element = doc.createElementNS(null, "myMdRecord");
                mdRecord.setContent(element);

                metadataRecords.add(mdRecord);
                item.setMetadataRecords(metadataRecords);

                properties.setContext(contextRef);
                properties.setContentModel(contentModelRef);

                item.setProperties(properties);
                setComponents(item, contentUrl);
            }
            catch (final ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return item;
        }

        private void setComponents(final Item item, final URL contentRef) {
            final Component component = new Component();
            setComponentProperties(component, contentRef);
            setComponentContent(component, contentRef);
            final Components components = new Components();
            components.add(component);
            item.setComponents(components);
        }

        private void setComponentContent(
            final Component component, final URL contentRef) {
            final ComponentContent content = new ComponentContent();
            content.setXLinkHref(contentRef.toString());
            content.setStorage("internal-managed");
            component.setContent(content);
        }

        private void setComponentProperties(
            final Component component, final URL contentRef) {
            final ComponentProperties properties = new ComponentProperties();

            properties.setDescription("Description?");
            properties.setFileName(contentRef.getFile());
            properties.setVisibility("isVisible?");
            properties.setContentCategory("which one?");

            component.setProperties(properties);
        }
    }
}