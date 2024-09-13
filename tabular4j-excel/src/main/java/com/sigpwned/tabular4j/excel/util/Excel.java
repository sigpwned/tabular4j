/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-excel
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
package com.sigpwned.tabular4j.excel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public final class Excel {
  private Excel() {}

  /**
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/List_of_file_signatures">https://en.wikipedia.org/wiki/List_of_file_signatures</a>
   */
  public static final byte[] XLSX_MAGIC_BYTES = new byte[] {(byte) 0x50, (byte) 0x4B};

  public static boolean isPossiblyXlsxFile(File file) throws IOException {
    return Arrays.equals(Excel.XLSX_MAGIC_BYTES, readNBytes(file, Excel.XLSX_MAGIC_BYTES.length));
  }

  /**
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/List_of_file_signatures">https://en.wikipedia.org/wiki/List_of_file_signatures</a>
   */
  public static final byte[] XLS_MAGIC_BYTES = new byte[] {(byte) 0xD0, (byte) 0xCF, (byte) 0x11,
      (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1};

  public static boolean isPossiblyXlsFile(File file) throws IOException {
    return Arrays.equals(Excel.XLS_MAGIC_BYTES, readNBytes(file, Excel.XLS_MAGIC_BYTES.length));
  }

  private static byte[] readNBytes(File f, int count) throws IOException {
    byte[] result;
    try (FileInputStream in = new FileInputStream(f)) {
      result = in.readNBytes(count);
    }
    return result;
  }
}
