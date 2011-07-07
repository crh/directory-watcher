package org.escidoc.watcher;

import com.google.common.base.Preconditions;

import org.escidoc.watcher.domain.FileUploader;
import org.escidoc.watcher.domain.Subscriber;
import org.escidoc.watcher.domain.internal.AppConfig;
import org.escidoc.watcher.domain.internal.FileEventImpl;
import org.escidoc.watcher.domain.internal.StdLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

public class EboxImpl implements Ebox {

  private final static Logger LOG = LoggerFactory.getLogger(EboxImpl.class);

  private static final String TO_MONITOR = "./sync-me";

  private final List<Subscriber> subscriberList = new ArrayList<Subscriber>();

  private Path monitoredPath;

  private AppConfig config;

  private WatchService service;

  private final String[] args;

  public EboxImpl(String[] args) {
    this.args = args;

  }

  public void start() throws FileNotFoundException, IOException,
      InterruptedException {
    loadConfiguration();
    registerSubscriber();
    setMonitoredFolder();
    init();
    startMonitoring();
  }

  private void startMonitoring() throws InterruptedException {
    for (;;) {
      usingPull(service);
    }
  }

  private void setMonitoredFolder() {
    switch (args.length) {
      case 0:
        useDefaultPath();
        break;
      case 1:
        monitoredPath = Paths.get(args[0]);
        break;
      default:
        LOG.error("One by one, Master...");
    }
  }

  public void loadConfiguration() throws FileNotFoundException, IOException {
    config = new FileConfiguration();
  }

  public void registerSubscriber() {
    Preconditions.checkNotNull(config, "config is null.");
    subscriberList.add(new StdLogger());
    subscriberList.add(new FileUploader(config));
  }

  private void useDefaultPath() {
    monitoredPath = Paths.get(TO_MONITOR);
  }

  private void init() throws IOException {
    service = FileSystems.getDefault().newWatchService();
    monitoredPath.register(service, StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_DELETE,
        StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.OVERFLOW);
  }

  public void usingPull(final WatchService service) throws InterruptedException {
    extract(service.take());
  }

  private void extract(final WatchKey key) {
    checkForValidity(key);
    key.reset();
  }

  private void checkForValidity(final WatchKey key) {
    if (isInvalid(key)) {
      LOG.warn("Invalid key: " + key);
    } else {
      processEvent(key);
    }
  }

  private void processEvent(final WatchKey key) {
    for (final WatchEvent<?> watchEvent : key.pollEvents()) {
      broadcast(watchEvent);
    }
  }

  private void broadcast(final WatchEvent<?> watchEvent) {
    for (final Subscriber s : subscriberList) {
      s.consume(new FileEventImpl(watchEvent, monitoredPath));
    }
  }

  private static boolean isInvalid(final WatchKey key) {
    return !key.isValid();
  }
}
