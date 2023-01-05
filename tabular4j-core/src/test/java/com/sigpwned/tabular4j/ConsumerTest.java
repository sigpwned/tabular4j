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

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.Test;
import com.google.common.io.Resources;
import com.sigpwned.tabular4j.consumer.TabularWorkbookConsumer;
import com.sigpwned.tabular4j.consumer.TabularWorksheetConsumer;
import com.sigpwned.tabular4j.consumer.WorkbookConsumer;
import com.sigpwned.tabular4j.consumer.WorksheetConsumer;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.io.source.UrlByteSource;
import com.sigpwned.tabular4j.model.TabularWorksheetCell;
import com.sigpwned.tabular4j.model.WorksheetCell;
import com.sigpwned.tabular4j.util.Spreadsheets;

public abstract class ConsumerTest {
  public String fileExtension;

  public ConsumerTest(String fileExtension) {
    this.fileExtension = requireNonNull(fileExtension);
  }

  @Test
  public void tabularWorkbookTest() throws IOException {
    List<String> events = new ArrayList<>();

    Spreadsheets.processTabularWorkbook(getByteSource(), new TabularWorkbookConsumer() {
      private int nameColumnIndex;

      @Override
      public void beginTabularWorksheet(int sheetIndex, String sheetName,
          List<String> columnNames) {
        events.add(format("beginTabularWorksheet(%d, %s)", sheetIndex, columnNames));
        nameColumnIndex = IntStream.range(0, columnNames.size())
            .filter(i -> columnNames.get(i).equals("Name")).findFirst().getAsInt();
      }

      @Override
      public void endTabularWorksheet() {
        events.add(format("endTabularWorksheet()"));
      }

      @Override
      public void tabularRow(int rowIndex, List<TabularWorksheetCell> cells) {
        events.add(format("tabularRow(%d, %s)", rowIndex,
            cells.get(nameColumnIndex).getValue(String.class)));
      }

      @Override
      public void beginTabularWorkbook() {
        events.add(format("beginTabularWorkbook()"));
      }

      @Override
      public void endTabularWorkbook() {
        events.add(format("endTabularWorkbook()"));
      }
    });

    assertThat(events,
        is(List.of("beginTabularWorkbook()",
            "beginTabularWorksheet(0, [Name, Player Code, Age, Team, League])",
            "tabularRow(1, Andrew McCutchen)", "tabularRow(2, Austin Adams)",
            "tabularRow(3, Andres Gimenez)", "tabularRow(4, Garrett Cleavinger)",
            "tabularRow(5, Mike Foltynewicz)", "endTabularWorksheet()", "endTabularWorkbook()")));
  }

  @Test
  public void tabularWorksheetTest() throws IOException {
    List<String> events = new ArrayList<>();

    Spreadsheets.processTabularActiveWorksheet(getByteSource(), new TabularWorksheetConsumer() {
      private int nameColumnIndex;

      @Override
      public void beginTabularWorksheet(int sheetIndex, String sheetName,
          List<String> columnNames) {
        events.add(format("beginTabularWorksheet(%d, %s)", sheetIndex, columnNames));
        nameColumnIndex = IntStream.range(0, columnNames.size())
            .filter(i -> columnNames.get(i).equals("Name")).findFirst().getAsInt();
      }

      @Override
      public void endTabularWorksheet() {
        events.add(format("endTabularWorksheet()"));
      }

      @Override
      public void tabularRow(int rowIndex, List<TabularWorksheetCell> cells) {
        events.add(format("tabularRow(%d, %s)", rowIndex,
            cells.get(nameColumnIndex).getValue(String.class)));
      }
    });

    assertThat(events,
        is(List.of("beginTabularWorksheet(0, [Name, Player Code, Age, Team, League])",
            "tabularRow(1, Andrew McCutchen)", "tabularRow(2, Austin Adams)",
            "tabularRow(3, Andres Gimenez)", "tabularRow(4, Garrett Cleavinger)",
            "tabularRow(5, Mike Foltynewicz)", "endTabularWorksheet()")));
  }

  @Test
  public void workbookTest() throws IOException {
    List<String> events = new ArrayList<>();

    Spreadsheets.processWorkbook(getByteSource(), new WorkbookConsumer() {
      @Override
      public void beginWorksheet(int sheetIndex, String sheetName) {
        events.add(format("beginWorksheet(%d)", sheetIndex));
      }

      @Override
      public void endWorksheet() {
        events.add(format("endWorksheet()"));
      }

      @Override
      public void row(int rowIndex, List<WorksheetCell> cells) {
        events.add(format("row(%d, %s)", rowIndex, cells.get(0).getValue(String.class)));
      }

      @Override
      public void beginWorkbook() {
        events.add(format("beginWorkbook()"));
      }

      @Override
      public void endWorkbook() {
        events.add(format("endWorkbook()"));
      }
    });

    assertThat(events,
        is(List.of("beginWorkbook()", "beginWorksheet(0)", "row(0, Name)",
            "row(1, Andrew McCutchen)", "row(2, Austin Adams)", "row(3, Andres Gimenez)",
            "row(4, Garrett Cleavinger)", "row(5, Mike Foltynewicz)", "endWorksheet()",
            "endWorkbook()")));
  }

  @Test
  public void worksheetTest() throws IOException {
    List<String> events = new ArrayList<>();

    Spreadsheets.processActiveWorksheet(getByteSource(), new WorksheetConsumer() {
      @Override
      public void beginWorksheet(int sheetIndex, String sheetName) {
        events.add(format("beginWorksheet(%d)", sheetIndex));
      }

      @Override
      public void endWorksheet() {
        events.add(format("endWorksheet()"));
      }

      @Override
      public void row(int rowIndex, List<WorksheetCell> cells) {
        events.add(format("row(%d, %s)", rowIndex, cells.get(0).getValue(String.class)));
      }
    });

    assertThat(events,
        is(List.of("beginWorksheet(0)", "row(0, Name)", "row(1, Andrew McCutchen)",
            "row(2, Austin Adams)", "row(3, Andres Gimenez)", "row(4, Garrett Cleavinger)",
            "row(5, Mike Foltynewicz)", "endWorksheet()")));
  }

  private ByteSource getByteSource() {
    return new UrlByteSource(Resources.getResource("mlb_players_2021." + fileExtension));
  }
}
