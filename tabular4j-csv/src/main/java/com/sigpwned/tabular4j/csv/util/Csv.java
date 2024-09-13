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
import java.io.UncheckedIOException;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.CsvRecord;
import com.sigpwned.csv4j.read.CsvReader;
import com.sigpwned.csv4j.read.MalformedRecordException;
import com.sigpwned.tabular4j.io.CharSource;

public final class Csv {
  private Csv() {}

  public static final String WORKSHEET_NAME = "csv";

  public static final CsvFormat STANDARD_FILE_FORMAT = CsvFormat.of('"', '"', ',');

  public static final CsvFormat TSV_FILE_FORMAT = CsvFormat.of('"', '"', '\t');

  private static final int PREVIEW_LENGTH = 64 * 1024;

  /**
   * Check if the given character source conforms to the given CSV format.
   * 
   * @param format The format to check against
   * @param source The character source
   * @return {@code true} if the character source conforms to the format, {@code false} otherwise
   * @throws IOException if an I/O error occurs
   * @see #conforms(CsvFormat, char[], int, int)
   */
  public static boolean conforms(CsvFormat format, CharSource source) throws IOException {
    char[] cbuf = new char[PREVIEW_LENGTH];

    int cbuflen;
    try (Reader r = source.getReader()) {
      cbuflen = readNChars(r, cbuf, 0, cbuf.length);
    }

    try {
      return conforms(format, cbuf, 0, cbuflen);
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
  }

  /**
   * Check if the given character buffer region conforms to the given CSV format. The buffer region
   * is expected to contain at least two records, and the records are expected to have at least two
   * columns. The records are expected to have the same number of columns. This is similar to the
   * approach taken by <a href="https://github.com/file/file">the Fine Free File Command</a>.
   * 
   * @param format The format to check against
   * @param cbuf The character buffer
   * @param off The offset into the character buffer
   * @param len The number of characters to inspect, starting at offset
   * @return {@code true} if the character buffer region conforms to the format, {@code false}
   *         otherwise
   * @throws UncheckedIOException if an I/O error occurs
   */
  public static boolean conforms(CsvFormat format, char[] cbuf, int off, int len) {
    // TODO Should we inspect headers?
    try (CsvReader r = new CsvReader(format, new CharArrayReader(cbuf, off, len))) {
      CsvRecord r1 = r.readNext();
      CsvRecord r2 = r.readNext();
      if (r1 != null && r2 != null && r1.size() > 1 && r2.size() > 1 && r1.size() == r2.size())
        return true;
    } catch (MalformedRecordException e) {
      // This is totally normal when parsing a CSV file with the wrong format
    } catch (IOException e) {
      // We're using in-memory I/O, so this should never happen, but JIC...
      throw new UncheckedIOException(e);
    }
    return false;
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
