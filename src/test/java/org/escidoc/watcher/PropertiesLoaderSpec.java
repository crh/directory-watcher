package org.escidoc.watcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLoaderSpec {

    private static final Logger LOG = LoggerFactory
        .getLogger(PropertiesLoaderSpec.class);

    private final Properties properties = new Properties();

    @Before
    public void setUp() throws FileNotFoundException, IOException {
        Properties props = System.getProperties();
        String configFullPath =
            props.get("user.home") + props.getProperty("file.separator")
                + ".escidoc" + props.getProperty("file.separator")
                + "escidocbox.properties";
        properties.load(new FileInputStream(configFullPath));
    }

    @Test
    public void itShouldCheckIfTheConfigFileExists() throws Exception {
    }

    @Test
    public void itShouldLoadTheContentInToMemory() {
    }

    @Test
    public void itShouldContainsAllRequiredKeys() {

        String contextId = (String) properties.get("contextId");
        String contentModelId = (String) properties.get("contentModelId");
        String containerId = (String) properties.get("containerId");
        String escidocUri = (String) properties.get("escidocUri");
        String loginname = (String) properties.get("loginname");
        String password = (String) properties.get("password");

        assertThat(contextId, notNullValue());
        assertThat(contentModelId, notNullValue());
        assertThat(containerId, notNullValue());
        assertThat(escidocUri, notNullValue());
        assertThat(loginname, notNullValue());
        assertThat(password, notNullValue());
    }
}