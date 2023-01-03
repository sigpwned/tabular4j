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
package com.sigpwned.spreadsheet4j.csv.read;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.stream.IntStream;
import com.sigpwned.csv4j.CsvRecord;
import com.sigpwned.csv4j.read.CsvReader;
import com.sigpwned.spreadsheet4j.csv.CsvConfigRegistry;
import com.sigpwned.spreadsheet4j.csv.CsvWorksheetCell;
import com.sigpwned.spreadsheet4j.csv.CsvWorksheetRow;
import com.sigpwned.spreadsheet4j.csv.util.Csv;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.io.CharSource;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorksheetRow;
import com.sigpwned.spreadsheet4j.util.MoreChardet;

public class CsvWorksheetReader implements WorksheetReader {
  private final CsvConfigRegistry config;
  private final CharSource source;
  private CsvReader delegate;
  private int rowIndex;

  public CsvWorksheetReader(CsvConfigRegistry config, ByteSource source) {
    this(config, MoreChardet.decode(source));
  }

  public CsvWorksheetReader(CsvConfigRegistry config, CharSource source) {
    this.config = requireNonNull(config);
    this.source = requireNonNull(source);
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

    if (delegate == null)
      delegate = new CsvReader(getSource().getReader());

    CsvRecord row = delegate.readNext();
    if (row != null) {
      result = new CsvWorksheetRow(rowIndex++, IntStream.range(0, row.getFields().size())
          .mapToObj(i -> new CsvWorksheetCell(i, row.getFields().get(i), getConfig())).toList());
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
    if (delegate != null)
      delegate.close();
  }

  /**
   * @return the config
   */
  public CsvConfigRegistry getConfig() {
    return config;
  }

  /**
   * @return the source
   */
  private CharSource getSource() {
    return source;
  }
}
