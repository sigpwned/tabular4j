package com.sigpwned.tabular4j.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;

public final class MoreFiles {
  private MoreFiles() {}

  /**
   * @see <a href=
   *      "https://stackoverflow.com/a/3282613/2103602">https://stackoverflow.com/a/3282613/2103602</a>
   */
  private static final boolean IS_OS_UNIX = File.separatorChar == '/';

  /**
   * A more secure way to create a temporary file. Addresses SonarCloud java:S5443, i.e., CWE-377,
   * CVE-2012-2451, CVE-2015-1838.
   */
  public static File createTempFile(String prefix, String suffix) throws IOException {
    File result;
    if (IS_OS_UNIX) {
      result = Files
          .createTempFile(prefix, suffix,
              PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")))
          .toFile();
    } else {
      result = Files.createTempFile(prefix, suffix).toFile();
      result.setReadable(true, true);
      result.setWritable(true, true);
      result.setExecutable(false);
    }
    return result;
  }
}
