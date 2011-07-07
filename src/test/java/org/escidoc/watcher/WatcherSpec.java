package org.escidoc.watcher;

import java.io.File;
import java.io.IOException;

import org.escidoc.watcher.domain.Directory;
import org.junit.Test;

public class WatcherSpec {

  private static final String TO_MONITOR = "/home/chh/projects/personal/directory-watcher/sync-me";

  @Test
  public void whenTheUserCreateAFileFileCreatedEventShouldBeFired()
      throws Exception {
  }

  private void createFileInSycMe(final Directory dir) throws IOException {
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
