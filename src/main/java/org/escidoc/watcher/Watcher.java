package org.escidoc.watcher;

public interface Watcher {

    void watch(Directory dir);

    void setSubscriber(Subscriber boss);

    void start();

}
