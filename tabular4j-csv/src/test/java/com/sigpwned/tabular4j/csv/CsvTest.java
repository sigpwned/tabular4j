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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
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
      }

      try (WorksheetReader r = factory.readActiveWorksheet(() -> new FileInputStream(tmp))) {
        WorksheetRow row = r.readRow();
        values.add(row.getCells().get(0).getValue(String.class));
        values.add(row.getCells().get(1).getValue(String.class));
      }
    } finally {
      tmp.delete();
    }

    assertThat(values, is(List.of("hello", "world")));
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
      }

      try (WorksheetReader r = factory.readActiveWorksheet(() -> new FileInputStream(tmp))) {
        WorksheetRow row = r.readRow();
        values.add(row.getCells().get(0).getValue(String.class));
        values.add(row.getCells().get(1).getValue(Integer.class));
        values.add(row.getCells().get(2).getValue(Instant.class));
      }
    } finally {
      tmp.delete();
    }

    assertThat(values, is(List.of("hello", 1, now)));
  }
}
