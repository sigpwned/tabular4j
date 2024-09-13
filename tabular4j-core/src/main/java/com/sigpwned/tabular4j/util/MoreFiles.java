/*-
 * =================================LICENSE_START==================================
 * tabular4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.tabular4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Optional;

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
      if (!result.setReadable(true, true))
        throw new IOException("Failed to set temporary file read permissions");
      if (!result.setWritable(true, true))
        throw new IOException("Failed to set temporary file write permissions");
      if (!result.setExecutable(false))
        throw new IOException("Failed to set temporary file execution permissions");
    }
    return result;
  }

  /**
   * Read the first {@code len} bytes from the given file. If the file is smaller than {@code len},
   * the entire file is read.
   * 
   * @param file The file to read
   * @param len The number of bytes to read
   * @return The bytes read
   * @throws IOException if an I/O error
   */
  public static byte[] readNBytes(File file, int len) throws IOException {
    byte[] result;
    try (InputStream in = new FileInputStream(file)) {
      result = in.readNBytes(len);
    }
    return result;
  }

  /**
   * Get the file extension from the given file. The file extension is the substring after the last
   * period (".") in the file name. If the file name does not contain a period, this method returns
   * an empty optional. If the file name ends with a period, this method returns an empty string. If
   * the file name starts with a period, this method returns an empty string.
   * 
   * @param file The file
   * @return The file extension, if any
   * @see Filenames#getExtension(String)
   */
  public static Optional<String> getFileExtension(File file) {
    return Filenames.getExtension(file.getName());
  }
}
