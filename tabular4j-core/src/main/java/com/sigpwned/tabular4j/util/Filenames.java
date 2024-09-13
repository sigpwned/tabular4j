/*-
 * =================================LICENSE_START==================================
 * tabular4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2024 Andy Boothe
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
