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
package com.sigpwned.spreadsheet4j.excel.read;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.sigpwned.spreadsheet4j.excel.ExcelConfigRegistry;
import com.sigpwned.spreadsheet4j.model.WorkbookReader;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;

public class ExcelWorkbookReader implements WorkbookReader {
  private final ExcelConfigRegistry config;
  private final Workbook delegate;
  private final List<ExcelWorksheetReader> worksheets;

  public ExcelWorkbookReader(ExcelConfigRegistry config, Workbook delegate) {
    this.config = requireNonNull(config);
    this.delegate = requireNonNull(delegate);

    List<ExcelWorksheetReader> worksheets = new ArrayList<>();
    for (int sheetIndex = 0; sheetIndex < getDelegate().getNumberOfSheets(); sheetIndex++) {
      Sheet worksheet = getDelegate().getSheetAt(sheetIndex);
      boolean active = getDelegate().getActiveSheetIndex() == sheetIndex;
      worksheets.add(new ExcelWorksheetReader(getConfig(), worksheet, sheetIndex, active));
    }
    if (worksheets.isEmpty())
      throw new IllegalArgumentException("no sheets");

    this.worksheets = unmodifiableList(worksheets);

    long activeSheetCount = getWorksheets().stream().filter(WorksheetReader::isActive).count();
    if (activeSheetCount < 1L)
      throw new IllegalArgumentException("no active sheet");
    else if (activeSheetCount > 1L)
      throw new IllegalArgumentException(
          "must have exactly 1 active sheet, not " + activeSheetCount);
  }

  /**
   * @return the config
   */
  public ExcelConfigRegistry getConfig() {
    return config;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<WorksheetReader> getWorksheets() {
    return (List) worksheets;
  }

  @Override
  public void close() throws IOException {
    getDelegate().close();
  }

  /**
   * @return the delegate
   */
  private Workbook getDelegate() {
    return delegate;
  }
}
