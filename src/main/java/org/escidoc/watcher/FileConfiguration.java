package org.escidoc.watcher;

import org.escidoc.watcher.domain.internal.AppConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class FileConfiguration implements AppConfig {
  private final Properties properties = new Properties();

  public FileConfiguration() throws FileNotFoundException, IOException {
    final Properties props = System.getProperties();
    final String configFullPath = props.get("user.home")
        + props.getProperty("file.separator") + ".escidoc"
        + props.getProperty("file.separator") + "escidocbox.properties";
    properties.load(new FileInputStream(configFullPath));
  }

  @Override
  public String getServiceAddress() {
    return (String) properties.get("escidocUri");
  }

  @Override
  public String getLoginName() {
    return (String) properties.get("loginname");
  }

  @Override
  public String getPassword() {
    return (String) properties.get("password");
  }

  @Override
  public String getContextId() {
    return (String) properties.get("contextId");
  }

  @Override
  public String getContentModelId() {
    return (String) properties.get("contentModelId");
  }

}
