package org.escidoc.watcher;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class WatcherSpec {

    private static final String TO_MONITOR =
        "/home/chh/projects/personal/directory-watcher/sync-me";

    @Test
    public void whenTheUserCreateAFileFileCreatedEventShouldBeFired()
        throws Exception {

        Watcher service = new WatcherImpl();

        Directory dir = new DirectoryImpl(new File(TO_MONITOR));
        service.watch(dir);

        service.start();

        Subscriber boss = new SubscriberImpl();
        service.setSubscriber(boss);

        createFileInSycMe(dir);

        // wait for max 10 seconds
    }

    private void createFileInSycMe(Directory dir) throws IOException {
        File.createTempFile("foo", "txt", dir.getFile());
    }

    @Test
    public void whenTheUserDeleteAFileFileDeletedEventShouldBeFired() {

    }

    @Test
    public void whenTheUserRenameAFileFileRenamedEventShouldBeFired() {

    }

    @Test
    public void whenTheUserMoveAFileToAnotherDirectoryFileMovedEventShouldBeFired() {
    }

    @Test
    public void whenTheUserCreateADirectoryThenDirectoryCreatedEventShouldBeFired() {

    }

}
