package org.escidoc.watcher;

public class WatcherImpl implements Watcher {

    private Directory dir;
    private Subscriber subscriber;

    public void watch(Directory dir) {
	this.dir = dir;
    }

    public void setSubscriber(Subscriber subscriber) {
	this.subscriber = subscriber;
    }

    public void start() {
	// TODO Auto-generated method stub
    }

}
