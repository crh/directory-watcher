package org.escidoc.watcher;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLoaderSpec {

    private static final Logger LOG = LoggerFactory
        .getLogger(PropertiesLoaderSpec.class);

    private final Properties properties = new Properties();

    @Test
    public void itShouldCheckIfTheConfigFileExists() throws Exception {
        Properties props = System.getProperties();
        Set<Entry<Object, Object>> entrySet = props.entrySet();
        for (Entry<Object, Object> entry : entrySet) {
            LOG.debug(entry.getKey() + ": " + entry.getValue());
        }

        // given
        String configFullPath =
            props.get("user.home") + props.getProperty("file.separator")
                + ".escidoc" + props.getProperty("file.separator")
                + "escidocbox.properties";
        properties.load(new FileInputStream(configFullPath));
    }

    @Test
    public void itShouldLoadTheContentInToMemory() {
    }

    @Test
    public void itShouldContainsAllRequiredKeys() {
        String contextId = (String) properties.get("contextId");
        String contentModelId = (String) properties.get("contentModelI");
        String containerId = (String) properties.get("containerI");
        String escidocUri = (String) properties.get("escidocUri");
        String loginname = (String) properties.get("loginname");
        String password = (String) properties.get("password");

        assertTrue(contextId != null);
        assertTrue(contentModelId != null);
        assertTrue(containerId != null);
        assertTrue(escidocUri != null);
        assertTrue(loginname != null);
        assertTrue(password != null);
    }
}
