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
package com.sigpwned.tabular4j.csv.read;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import java.io.IOException;
import java.util.stream.IntStream;
import com.sigpwned.csv4j.CsvRecord;
import com.sigpwned.csv4j.read.CsvReader;
import com.sigpwned.tabular4j.csv.CsvConfigRegistry;
import com.sigpwned.tabular4j.csv.CsvWorksheetCell;
import com.sigpwned.tabular4j.csv.CsvWorksheetRow;
import com.sigpwned.tabular4j.csv.util.Csv;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetRow;

public class CsvWorksheetReader implements WorksheetReader {
  private final CsvConfigRegistry config;
  private final CsvReader reader;
  private int rowIndex;

  public CsvWorksheetReader(CsvConfigRegistry config, CsvReader reader) {
    this.config = requireNonNull(config);
    this.reader = requireNonNull(reader);
  }

  @Override
  public int getSheetIndex() {
    return 0;
  }

  @Override
  public String getSheetName() {
    return Csv.WORKSHEET_NAME;
  }

  @Override
  public WorksheetRow readRow() throws IOException {
    WorksheetRow result;

    CsvRecord row = getReader().readNext();
    if (row != null) {
      result = new CsvWorksheetRow(rowIndex++,
          IntStream.range(0, row.getFields().size())
              .mapToObj(i -> new CsvWorksheetCell(i, row.getFields().get(i), getConfig()))
              .collect(toList()));
    } else {
      result = null;
    }

    return result;
  }

  @Override
  public boolean isActive() {
    // CSV workbooks only have one sheet, so sheets are always active by definition.
    return true;
  }

  @Override
  public void close() throws IOException {
    getReader().close();
  }

  /**
   * @return the config
   */
  public CsvConfigRegistry getConfig() {
    return config;
  }

  /**
   * @return the reader
   */
  private CsvReader getReader() {
    return reader;
  }
}
