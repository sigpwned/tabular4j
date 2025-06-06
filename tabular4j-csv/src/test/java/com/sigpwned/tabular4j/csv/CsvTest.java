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
package com.sigpwned.tabular4j.csv;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.sigpwned.tabular4j.csv.util.MoreChardet;
import com.sigpwned.tabular4j.exception.InvalidFileSpreadsheetException;
import com.sigpwned.tabular4j.model.WorksheetCellDefinition;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetRow;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public class CsvTest {
  @Test
  public void smokeTest() throws IOException {
    CsvSpreadsheetFormatFactory factory = new CsvSpreadsheetFormatFactory();

    List<Object> values = new ArrayList<>();
    File tmp = File.createTempFile("workbook.", ".csv");
    try {
      try (WorksheetWriter w = factory.writeActiveWorksheet(() -> new FileOutputStream(tmp))) {
        w.writeRow(List.of(WorksheetCellDefinition.ofValue("hello"),
            WorksheetCellDefinition.ofValue("world")));
        w.writeRow(List.of(WorksheetCellDefinition.ofValue("alpha"),
            WorksheetCellDefinition.ofValue("bravo")));
      }

      try (WorksheetReader r = factory.readActiveWorksheet(() -> new FileInputStream(tmp))) {
        WorksheetRow row1 = r.readRow();
        values.add(row1.getCells().get(0).getValue(String.class));
        values.add(row1.getCells().get(1).getValue(String.class));

        WorksheetRow row2 = r.readRow();
        values.add(row2.getCells().get(0).getValue(String.class));
        values.add(row2.getCells().get(1).getValue(String.class));
      }
    } finally {
      tmp.delete();
    }

    assertThat(values, is(List.of("hello", "world", "alpha", "bravo")));
  }

  @Test
  public void valueMappingTest() throws IOException {
    CsvSpreadsheetFormatFactory factory = new CsvSpreadsheetFormatFactory();

    Instant now = Instant.now();

    List<Object> values = new ArrayList<>();
    File tmp = File.createTempFile("workbook.", ".csv");
    try {
      try (WorksheetWriter w = factory.writeActiveWorksheet(() -> new FileOutputStream(tmp))) {
        w.writeRow(List.of(WorksheetCellDefinition.ofValue("hello"),
            WorksheetCellDefinition.ofValue(1), WorksheetCellDefinition.ofValue(now)));
        w.writeRow(List.of(WorksheetCellDefinition.ofValue("world"),
            WorksheetCellDefinition.ofValue(2), WorksheetCellDefinition.ofValue(now)));
      }

      try (WorksheetReader r = factory.readActiveWorksheet(() -> new FileInputStream(tmp))) {
        WorksheetRow row1 = r.readRow();
        values.add(row1.getCells().get(0).getValue(String.class));
        values.add(row1.getCells().get(1).getValue(Integer.class));
        values.add(row1.getCells().get(2).getValue(Instant.class));

        WorksheetRow row2 = r.readRow();
        values.add(row2.getCells().get(0).getValue(String.class));
        values.add(row2.getCells().get(1).getValue(Integer.class));
        values.add(row2.getCells().get(2).getValue(Instant.class));
      }
    } finally {
      tmp.delete();
    }

    assertThat(values, is(List.of("hello", 1, now, "world", 2, now)));
  }

  @Test(expected = InvalidFileSpreadsheetException.class)
  public void xlsxNotAcceptableTest() throws IOException {
    final URL url = getClass().getResource("example.xlsx");
    new CsvSpreadsheetFormatFactory().readWorkbook(url::openStream);
  }

  @Test(expected = InvalidFileSpreadsheetException.class)
  public void xlsNotAcceptableTest() throws IOException {
    final URL url = getClass().getResource("example.xls");
    new CsvSpreadsheetFormatFactory().readWorkbook(url::openStream);
  }

  @Test
  public void bomDiscardTest() throws IOException {
    final URL url = getClass().getResource("keywords.csv");

    // The file should have a UTF-8 BOM
    try (BufferedReader r =
        new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
      // Make sure the BOM is discarded
      String line = r.readLine();
      assertThat(line, is("\uFEFFkeyword,interest"));
    }

    // The decoded character stream should not have a BOM
    try (BufferedReader r = new BufferedReader(MoreChardet.decode(url::openStream).getReader())) {
      // Make sure the BOM is discarded
      String line = r.readLine();
      assertThat(line, is("keyword,interest"));
    }
  }
}
