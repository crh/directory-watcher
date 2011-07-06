package org.escidoc.watcher;

import com.google.common.base.Preconditions;

import org.escidoc.watcher.domain.FileUploader;
import org.escidoc.watcher.domain.Subscriber;
import org.escidoc.watcher.domain.internal.AppConfig;
import org.escidoc.watcher.domain.internal.FileEventImpl;
import org.escidoc.watcher.domain.internal.StdLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class App {

  private static final Logger LOG = LoggerFactory.getLogger(App.class);

  private static final String TO_MONITOR = "./sync-me";

  private static Path monitoredPath;

  private static List<Subscriber> subscriberList = new ArrayList<Subscriber>();

  private static AppConfig config;

  public static void main(final String... args) throws Exception {
    config = new FileConfiguration();
    subscribe();
    switch (args.length) {
      case 0:
        useDefaultPath();
        break;
      case 1:
        monitoredPath = Paths.get(args[0]);
        final WatchService service = init();
        for (;;) {
          usingPull(service);
        }
      default:
        LOG.error("One by one, Master...");
    }
  }

  private static void subscribe() {
    Preconditions.checkNotNull(config, "config is null.");
    subscriberList.add(new StdLogger());
    subscriberList.add(new FileUploader(config));
  }

  private static void useDefaultPath() {
    monitoredPath = Paths.get(TO_MONITOR);
  }

  private static WatchService init() throws IOException {
    final WatchService service = FileSystems.getDefault().newWatchService();
    monitoredPath.register(service, StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_DELETE,
        StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.OVERFLOW);
    return service;
  }

  private static void usingPull(final WatchService service)
      throws InterruptedException {
    extract(service.take());
  }

  private static void extract(final WatchKey key) {
    checkForValidity(key);
    key.reset();
  }

  private static void checkForValidity(final WatchKey key) {
    if (isInvalid(key)) {
      LOG.warn("Invalid key: " + key);
    } else {
      processEvent(key);
    }
  }

  private static void processEvent(final WatchKey key) {
    for (final WatchEvent<?> watchEvent : key.pollEvents()) {
      broadcast(watchEvent);
    }
  }

  private static void broadcast(final WatchEvent<?> watchEvent) {
    for (final Subscriber s : subscriberList) {
      s.consume(new FileEventImpl(watchEvent, monitoredPath));
    }
  }

  private static boolean isInvalid(final WatchKey key) {
    return !key.isValid();
  }
}