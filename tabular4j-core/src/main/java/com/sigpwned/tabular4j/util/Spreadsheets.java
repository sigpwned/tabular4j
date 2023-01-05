/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-core
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
package com.sigpwned.tabular4j.util;

import java.io.IOException;
import com.sigpwned.tabular4j.SpreadsheetFactory;
import com.sigpwned.tabular4j.consumer.TabularWorkbookConsumer;
import com.sigpwned.tabular4j.consumer.TabularWorksheetConsumer;
import com.sigpwned.tabular4j.consumer.WorkbookConsumer;
import com.sigpwned.tabular4j.consumer.WorksheetConsumer;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.model.TabularWorkbookReader;
import com.sigpwned.tabular4j.model.TabularWorksheetReader;
import com.sigpwned.tabular4j.model.TabularWorksheetRow;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetRow;

public final class Spreadsheets {
  private Spreadsheets() {}

  public static void processWorkbook(ByteSource source, WorkbookConsumer consumer)
      throws IOException {
    try (WorkbookReader workbook = SpreadsheetFactory.getInstance().readWorkbook(source)) {
      consumer.beginWorkbook();
      for (int i = 0; i < workbook.getWorksheetCount(); i++) {
        WorksheetReader worksheet = workbook.getWorksheet(i);
        consumer.beginWorksheet(worksheet.getSheetIndex(), worksheet.getSheetName());
        for (WorksheetRow row : worksheet)
          consumer.row(row.getRowIndex(), row.getCells());
        consumer.endWorksheet();
      }
      consumer.endWorkbook();
    }
  }

  public static void processActiveWorksheet(ByteSource source, WorksheetConsumer consumer)
      throws IOException {
    try (WorksheetReader worksheet = SpreadsheetFactory.getInstance().readActiveWorksheet(source)) {
      consumer.beginWorksheet(worksheet.getSheetIndex(), worksheet.getSheetName());
      for (WorksheetRow row : worksheet)
        consumer.row(row.getRowIndex(), row.getCells());
      consumer.endWorksheet();
    }
  }

  public static void processTabularWorkbook(ByteSource source, TabularWorkbookConsumer consumer)
      throws IOException {
    try (TabularWorkbookReader workbook =
        SpreadsheetFactory.getInstance().readTabularWorkbook(source)) {
      consumer.beginTabularWorkbook();
      for (int i = 0; i < workbook.getWorksheetCount(); i++) {
        TabularWorksheetReader worksheet = workbook.getWorksheet(i);
        consumer.beginTabularWorksheet(worksheet.getSheetIndex(), worksheet.getSheetName(),
            worksheet.getColumnNames());
        for (TabularWorksheetRow row : worksheet)
          consumer.tabularRow(row.getRowIndex(), row.getCells());
        consumer.endTabularWorksheet();
      }
      consumer.endTabularWorkbook();
    }
  }

  public static void processTabularActiveWorksheet(ByteSource source,
      TabularWorksheetConsumer consumer) throws IOException {
    try (TabularWorksheetReader worksheet =
        SpreadsheetFactory.getInstance().readActiveTabularWorksheet(source)) {
      consumer.beginTabularWorksheet(worksheet.getSheetIndex(), worksheet.getSheetName(),
          worksheet.getColumnNames());
      for (TabularWorksheetRow row : worksheet)
        consumer.tabularRow(row.getRowIndex(), row.getCells());
      consumer.endTabularWorksheet();
    }
  }
}
