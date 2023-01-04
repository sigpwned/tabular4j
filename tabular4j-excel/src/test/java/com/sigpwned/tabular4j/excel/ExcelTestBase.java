/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-xlsx
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
package com.sigpwned.tabular4j.excel;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.model.WorksheetCellDefinition;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetRow;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public abstract class ExcelTestBase<F extends ExcelSpreadsheetFormatFactory> {
  public F format;

  protected ExcelTestBase(F format) {
    this.format = format;
  }

  @Test
  public void smokeTest() throws IOException {
    List<Object> values = new ArrayList<>();
    File tmp = File.createTempFile("workbook.", "." + format.getDefaultFileExtension());
    try {
      try (WorksheetWriter w = format.writeActiveWorksheet(tmp)) {
        w.writeRow(List.of(WorksheetCellDefinition.ofValue("hello"),
            WorksheetCellDefinition.ofValue("world")));
      }

      try (WorksheetReader r = format.readActiveWorksheet(tmp)) {
        WorksheetRow row = r.readRow();
        values.add(row.getCells().get(0).getValue(String.class));
        values.add(row.getCells().get(1).getValue(String.class));
      }
    } finally {
      tmp.delete();
    }

    assertThat(values, is(List.of("hello", "world")));
  }

  @Test(expected = IOException.class)
  public void mergedRegionTest() throws Exception {
    ByteSource source = () -> Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("has-merged-region." + format.getDefaultFileExtension());

    try (WorksheetReader r = format.readActiveWorksheet(source)) {
      // This should throw an exception
      r.readRow();
    }
  }

  @Test
  public void hiddenRowsAndColsTest() throws Exception {
    ByteSource source = () -> Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("has-hidden-rows-and-cols." + format.getDefaultFileExtension());

    List<List<String>> content = new ArrayList<>();
    try (WorksheetReader r = format.readActiveWorksheet(source)) {
      // This should throw an exception
      for (WorksheetRow row : r) {
        content.add(row.getCells().stream().map(c -> c.getValue(String.class)).collect(toList()));
      }
    }

    assertThat(content, is(List.of(List.of("header1", "header3"), List.of("alpha", "charlie"),
        List.of("golf", "india"))));
  }

  @Test
  public void activeFiltersTest() throws Exception {
    ByteSource source = () -> Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("has-active-filters." + format.getDefaultFileExtension());

    List<List<String>> content = new ArrayList<>();
    try (WorksheetReader r = format.readActiveWorksheet(source)) {
      // This should throw an exception
      for (WorksheetRow row : r) {
        content.add(row.getCells().stream().map(c -> c.getValue(String.class)).collect(toList()));
      }
    }

    assertThat(content, is(List.of(List.of("header1", "header2", "header3"),
        List.of("delta", "echo", "foxtrot"), List.of("golf", "hotel", "india"))));
  }

  @Test
  public void activeSheetTest() throws Exception {
    ByteSource source = () -> Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("has-multiple-sheets." + format.getDefaultFileExtension());

    List<List<String>> content = new ArrayList<>();
    try (WorksheetReader r = format.readActiveWorksheet(source)) {
      // This should throw an exception
      for (WorksheetRow row : r) {
        content.add(row.getCells().stream().map(c -> c.getValue(String.class)).collect(toList()));
      }
    }

    assertThat(content,
        is(List.of(List.of("delta", "echo", "foxtrot"), List.of("s101", "s102", "s103"),
            List.of("s104", "s105", "s106"), List.of("s107", "s108", "s109"))));
  }
}
