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
package com.sigpwned.tabular4j;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.sigpwned.tabular4j.model.TabularWorkbookReader;
import com.sigpwned.tabular4j.model.TabularWorkbookWriter;
import com.sigpwned.tabular4j.model.TabularWorksheetReader;
import com.sigpwned.tabular4j.model.TabularWorksheetRow;
import com.sigpwned.tabular4j.model.TabularWorksheetRowWriter;
import com.sigpwned.tabular4j.model.WorksheetCellDefinition;

public abstract class TabularTest {
  public String fileExtension;

  public TabularTest(String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public static final List<String> HEADERS = List.of("alpha", "bravo");

  public static final List<List<String>> READ_ROWS =
      List.of(List.of("hello", "world"), List.of("a", "b"), asList("1", null));

  public static final List<List<WorksheetCellDefinition>> WRITE_ROWS =
      List.of(List.of("hello", "world"), List.of("a", "b", "c"), List.of("1")).stream()
          .map(
              xs -> xs.stream().map(WorksheetCellDefinition::ofValue).collect(toUnmodifiableList()))
          .collect(toUnmodifiableList());

  @Test
  public void smokeTest() throws IOException {
    ByteArrayOutputStream drain = new ByteArrayOutputStream();

    try (TabularWorkbookWriter wb =
        new SpreadsheetFactory().writeTabularWorkbook(() -> drain, fileExtension)) {
      try (TabularWorksheetRowWriter ws = wb.getWorksheet("sheet").writeHeaders(HEADERS)) {
        for (List<WorksheetCellDefinition> row : WRITE_ROWS)
          ws.writeRow(row);
      }
    }

    byte[] output = drain.toByteArray();

    List<List<String>> rows = new ArrayList<>();
    try (TabularWorkbookReader wb =
        new SpreadsheetFactory().readTabularWorkbook(() -> new ByteArrayInputStream(output))) {
      try (TabularWorksheetReader ws = wb.getWorksheet(0)) {
        assertThat(ws.getColumnNames(), is(HEADERS));
        for (TabularWorksheetRow row : ws)
          rows.add(row.getCells().stream().map(c -> c.getValue(String.class)).collect(toList()));
      }
    }

    assertThat(rows, is(READ_ROWS));
  }
}
