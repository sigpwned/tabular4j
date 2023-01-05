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
package com.sigpwned.tabular4j.test;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.sigpwned.tabular4j.SpreadsheetFormatFactory;
import com.sigpwned.tabular4j.exception.InvalidFileSpreadsheetException;
import com.sigpwned.tabular4j.forwarding.ForwardingWorkbookReader;
import com.sigpwned.tabular4j.forwarding.ForwardingWorkbookWriter;
import com.sigpwned.tabular4j.forwarding.ForwardingWorksheetReader;
import com.sigpwned.tabular4j.forwarding.ForwardingWorksheetWriter;
import com.sigpwned.tabular4j.io.ByteSink;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorkbookWriter;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public class TestSpreadsheetFormatFactory implements SpreadsheetFormatFactory {

  @Override
  public WorkbookReader readWorkbook(ByteSource source) throws IOException {
    return new ForwardingWorkbookReader(new TestWorkbookReader(lines(source)));
  }

  @Override
  public WorksheetReader readActiveWorksheet(ByteSource source) throws IOException {
    return new ForwardingWorksheetReader(new TestWorksheetReader(lines(source).listIterator()));
  }

  @Override
  public WorkbookWriter writeWorkbook(ByteSink sink) throws IOException {
    return new ForwardingWorkbookWriter(new TestWorkbookWriter(sink));
  }

  @Override
  public WorksheetWriter writeActiveWorksheet(ByteSink sink) throws IOException {
    final List<List<String>> rows = new ArrayList<>();
    return new ForwardingWorksheetWriter(new TestWorksheetWriter(rows)) {
      @Override
      public void close() throws IOException {
        try {
          write(sink, rows);
        } finally {
          super.close();
        }
      }
    };
  }

  @Override
  public String getDefaultFileExtension() {
    return "test";
  }

  /**
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/List_of_file_signatures">https://en.wikipedia.org/wiki/List_of_file_signatures</a>
   */
  private static final byte[] XLSX_MAGIC_BYTES = new byte[] {(byte) 0x50, (byte) 0x4B};

  /**
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/List_of_file_signatures">https://en.wikipedia.org/wiki/List_of_file_signatures</a>
   */
  private static final byte[] XLS_MAGIC_BYTES = new byte[] {(byte) 0xD0, (byte) 0xCF, (byte) 0x11,
      (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1};

  /**
   * Our CSVs always start with a double quote.
   */
  private static final byte[] CSV_MAGIC_BYTES = new byte[] {'"'};

  private static List<List<String>> lines(ByteSource source) throws IOException {
    byte[] buf;
    try (InputStream in = source.getInputStream()) {
      buf = in.readAllBytes();
    }
    for (byte[] blacklist : new byte[][] {XLS_MAGIC_BYTES, XLSX_MAGIC_BYTES, CSV_MAGIC_BYTES}) {
      if (Arrays.compare(buf, 0, blacklist.length, blacklist, 0, blacklist.length) == 0)
        throw new InvalidFileSpreadsheetException();
    }

    List<List<String>> result;
    try (BufferedReader r =
        new BufferedReader(new StringReader(new String(buf, StandardCharsets.UTF_8)))) {
      result = r.lines().map(s -> asList(s.strip().split(","))).collect(toList());
    }
    return result;
  }

  /* default */ static void write(ByteSink sink, List<List<String>> rows) throws IOException {
    try (Writer out = sink.asCharSink(StandardCharsets.UTF_8).getWriter()) {
      for (List<String> row : rows) {
        out.write(String.join(",", row));
        out.write("\n");
      }
    }
  }
}
