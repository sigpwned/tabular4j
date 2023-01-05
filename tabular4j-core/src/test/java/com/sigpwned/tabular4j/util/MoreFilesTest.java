package com.sigpwned.tabular4j.util;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class MoreFilesTest {
  @Test
  public void test() throws IOException {
    File file = MoreFiles.createTempFile("alpha.", ".bravo");
    try {
      assertThat(file.getName(), allOf(startsWith("alpha."), endsWith(".bravo")));
    } finally {
      file.delete();
    }
  }
}
