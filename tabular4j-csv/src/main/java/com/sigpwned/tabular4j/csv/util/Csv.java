/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-csv
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
package com.sigpwned.tabular4j.csv.util;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.CsvRecord;
import com.sigpwned.csv4j.read.CsvReader;
import com.sigpwned.csv4j.read.MalformedRecordException;
import com.sigpwned.tabular4j.csv.CsvConfigRegistry;
import com.sigpwned.tabular4j.io.CharSource;

public final class Csv {
  private Csv() {}

  public static final String WORKSHEET_NAME = "csv";

  public static final CsvFormat STANDARD_FILE_FORMAT = CsvFormat.of('"', '"', ',');

  public static final CsvFormat TSV_FILE_FORMAT = CsvFormat.of('"', '"', '\t');

  private static final int PREVIEW_LENGTH = 64 * 1024;

  /**
   * Detect the format of the given CSV-formatted character source.
   * 
   * @param config The configuration to use. Only the formats in this configuration will be tested.
   * @param source The character source to inspect
   * @return The detected format, or empty if no format was detected
   * @throws IOException if an I/O error
   */
  public static Optional<CsvFormat> detectFormat(CsvConfigRegistry config, CharSource source)
      throws IOException {
    char[] cbuf = new char[PREVIEW_LENGTH];

    int cbuflen;
    try (Reader r = source.getReader()) {
      cbuflen = readNChars(r, cbuf, 0, cbuf.length);
    }

    return detectFormat(config, cbuf, 0, cbuflen);
  }

  /**
   * Detect the format of the given CSV-formatted character buffer region.
   * 
   * @param config The configuration to use. Only the formats in this configuration will be tested.
   * @param cbuf The character buffer to inspect
   * @param off The offset into the character buffer
   * @param len The number of characters to inspect, starting at offset
   * @return The detected format, or empty if no format was detected
   * @throws IOException if an I/O error
   */
  public static Optional<CsvFormat> detectFormat(CsvConfigRegistry config, char[] cbuf, int off,
      int len) throws IOException {
    // TODO Should we inspect headers?
    // TODO Should we adapt buffer to be bigger?
    // TODO Should we read until we get 3 newlines, or something? What about quoting?
    // TODO What if both rows have 1 field because we're using the wrong separator?
    // TODO What if headers and first data row have different number of fields?
    for (CsvFormat format : config.getFormats()) {
      try (CsvReader r = new CsvReader(format, new CharArrayReader(cbuf, off, len))) {
        CsvRecord r1 = r.readNext();
        CsvRecord r2 = r.readNext();
        if (r1 != null && r2 != null && r1.size() == r2.size())
          return Optional.of(format);
      } catch (MalformedRecordException e) {
        // This is totally normal when parsing a CSV file with the wrong format
      }
    }
    return Optional.empty();
  }

  /**
   * Read n characters from the given reader, returning fewer only on EOF.
   * 
   * @param r the reader to read from
   * @param n the number of characters to read
   * @return the characters read
   * @throws IOException if an I/O error occurs
   */
  private static int readNChars(Reader r, char[] cbuf, int off, int len) throws IOException {
    int total = 0;
    while (total < len) {
      int nread = r.read(cbuf, off + total, len - total);
      if (nread == -1)
        break;
      total = total + nread;
    }
    return total;
  }
}
