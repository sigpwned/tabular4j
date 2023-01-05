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

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.sigpwned.tabular4j.SpreadsheetFactory;
import com.sigpwned.tabular4j.TabularTest;
import com.sigpwned.tabular4j.io.sink.ByteArrayByteSink;
import com.sigpwned.tabular4j.io.source.ByteArrayByteSource;
import com.sigpwned.tabular4j.model.TabularWorkbookReader;
import com.sigpwned.tabular4j.model.TabularWorkbookWriter;
import com.sigpwned.tabular4j.model.TabularWorksheetReader;
import com.sigpwned.tabular4j.model.TabularWorksheetRow;
import com.sigpwned.tabular4j.model.TabularWorksheetRowWriter;
import com.sigpwned.tabular4j.model.WorksheetCellDefinition;

public class TestTabularTest extends TabularTest {
  public TestTabularTest() {
    super("test");
  }

  // TODO Convert to a text block if and when we change our Java target
  public static final String TEXT = "alpha,bravo\n" + "hello,world\n" + "a,b\n" + "1,\n";

  @Test
  public void workbookWriteTest() throws IOException {
    ByteArrayByteSink sink = new ByteArrayByteSink();

    try (TabularWorkbookWriter wb = new SpreadsheetFactory().writeTabularWorkbook(sink, "test")) {
      try (TabularWorksheetRowWriter ws = wb.getWorksheet("test").writeHeaders(HEADERS)) {
        for (List<WorksheetCellDefinition> row : WRITE_ROWS)
          ws.writeRow(row);
      }
    }

    assertThat(new String(sink.getBytes(), StandardCharsets.UTF_8), is(TEXT));
  }

  @Test
  public void worksheetWriteTest() throws IOException {
    ByteArrayByteSink sink = new ByteArrayByteSink();

    try (TabularWorksheetRowWriter ws =
        new SpreadsheetFactory().writeTabularActiveWorksheet(sink, "test").writeHeaders(HEADERS)) {
      for (List<WorksheetCellDefinition> row : WRITE_ROWS)
        ws.writeRow(row);
    }

    assertThat(new String(sink.getBytes(), StandardCharsets.UTF_8), is(TEXT));
  }

  @Test
  public void workbookReadTest() throws IOException {
    ByteArrayByteSource source = new ByteArrayByteSource(TEXT.getBytes(StandardCharsets.UTF_8));

    try (TabularWorkbookReader wb = new SpreadsheetFactory().readTabularWorkbook(source)) {
      try (TabularWorksheetReader ws = wb.getWorksheet(0)) {
        assertThat(ws.getColumnNames(), is(HEADERS));

        List<List<String>> rows = new ArrayList<>();
        for (TabularWorksheetRow row : ws) {
          rows.add(row.getCells().stream().map(c -> c.getValue(String.class)).collect(toList()));
        }

        assertThat(rows, is(READ_ROWS));
      }
    }
  }

  @Test
  public void worksheetReadTest() throws IOException {
    ByteArrayByteSource source = new ByteArrayByteSource(TEXT.getBytes(StandardCharsets.UTF_8));

    try (TabularWorksheetReader ws = new SpreadsheetFactory().readActiveTabularWorksheet(source)) {
      assertThat(ws.getColumnNames(), is(HEADERS));

      List<List<String>> rows = new ArrayList<>();
      for (TabularWorksheetRow row : ws) {
        rows.add(row.getCells().stream().map(c -> c.getValue(String.class)).collect(toList()));
      }

      assertThat(rows, is(READ_ROWS));
    }
  }
}
