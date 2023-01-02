/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
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
package com.sigpwned.spreadsheet4j.util;

import com.sigpwned.spreadsheet4j.consumer.WorkbookConsumer;
import com.sigpwned.spreadsheet4j.consumer.WorksheetConsumer;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.model.RowIterator;
import com.sigpwned.spreadsheet4j.model.WorkbookReader;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;

public class Tabular {
  public static void consume(ByteSource source, WorkbookConsumer consumer) {}

  public static void consume(ByteSource source, WorksheetConsumer consumer) {}

  public static WorksheetReader read(ByteSource source) {

  }

  public static void consume(WorkbookReader workbook, WorkbookConsumer consumer) {
    consumer.beginWorkbook();

    for (int si = 0; si < workbook.size(); si++) {
      WorksheetReader worksheet = workbook.getWorksheetByIndex(si);

      try (RowIterator rows = worksheet.iterator()) {
        consumer.beginWorksheet(si, worksheet.getSheetName(), rows.getHeaders());

        int ri = 0;
        while (rows.hasNext())
          consumer.row(ri++, rows.next().getCells());
      }

      consumer.endWorksheet();
    }

    consumer.endWorkbook();
  }
}
