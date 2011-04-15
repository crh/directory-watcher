package org.escidoc.watcher.domain;

public interface Watcher {

    void watch(Directory dir);

    void setSubscriber(Subscriber boss);

    void start();

}
