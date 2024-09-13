package com.sigpwned.tabular4j.util;

import java.util.Optional;

public final class Filenames {
  private Filenames() {}

  /**
   * Get the extension of a filename. The extension is considered to be all the text after the last
   * period in the filename. If there is no period in the filename, the extension is empty. If the
   * period is the last character in the filename, the extension is empty. If the filename starts
   * with a period, the extension is empty.
   * 
   * @param filename the filename, e.g., "foo.txt"
   * @return the extension, e.g., "txt"
   */
  public static Optional<String> getExtension(String filename) {
    if (filename == null)
      throw new NullPointerException();
    if (filename.charAt(0) == '.')
      return Optional.empty();
    int index = filename.lastIndexOf('.');
    if (index == -1)
      return Optional.empty();
    return Optional.of(filename.substring(index + 1, filename.length()));
  }
}
