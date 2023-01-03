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
package com.sigpwned.spreadsheet4j.xlsx.read;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorksheetRow;
import com.sigpwned.spreadsheet4j.xlsx.XlsxConfigRegistry;
import com.sigpwned.spreadsheet4j.xlsx.XlsxWorksheetCell;
import com.sigpwned.spreadsheet4j.xlsx.XlsxWorksheetRow;

/**
 * Fails to read sheets with merged regions. Respects hidden columns.
 */
public class XlsxWorksheetReader implements WorksheetReader {
  private static final int FIRST_ROW_INDEX = 0;

  private final XlsxConfigRegistry config;
  private final XSSFSheet worksheet;
  private final int sheetIndex;
  private final boolean active;
  private int rowIndex;
  private Map<Integer, Boolean> hidden;

  public XlsxWorksheetReader(XlsxConfigRegistry config, XSSFSheet worksheet, int sheetIndex,
      boolean active) {
    this.config = requireNonNull(config);
    this.worksheet = requireNonNull(worksheet);
    this.sheetIndex = sheetIndex;
    this.active = active;
    this.rowIndex = FIRST_ROW_INDEX;
    this.hidden = new HashMap<>();
  }

  @Override
  public WorksheetRow readRow() throws IOException {
    if (getWorksheet().getFirstRowNum() == -1)
      return null;

    if (rowIndex > getWorksheet().getLastRowNum())
      return null;

    if (rowIndex == FIRST_ROW_INDEX) {
      if (getWorksheet().getNumMergedRegions() != 0)
        throw new IOException(
            format("Cannot read sheet %s because it contains merged regions", getSheetName()));
    }

    WorksheetRow result = null;
    while (result == null && rowIndex <= getWorksheet().getLastRowNum()) {
      if (rowIndex < getWorksheet().getFirstRowNum()) {
        result = new XlsxWorksheetRow(rowIndex, List.of());
      } else {
        XSSFRow row = getWorksheet().getRow(rowIndex);
        if (row.getFirstCellNum() == -1) {
          result = new XlsxWorksheetRow(rowIndex, List.of());
          break;
        } else if (row.isFormatted() && row.getRowStyle().getHidden()) {
          // Skip this row, since it's hidden.
        } else if (row.getZeroHeight()) {
          // This is another way to hide a row
        } else {
          List<XlsxWorksheetCell> cells = new ArrayList<>();
          for (int i = 0; i < row.getFirstCellNum(); i++)
            if (!hidden.computeIfAbsent(i, getWorksheet()::isColumnHidden))
              cells.add(new XlsxWorksheetCell(getConfig(), i, null));
          for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++)
            if (!hidden.computeIfAbsent(i, getWorksheet()::isColumnHidden))
              cells.add(new XlsxWorksheetCell(getConfig(), i, row.getCell(i)));
          result = new XlsxWorksheetRow(rowIndex, cells);
        }
      }
      rowIndex = rowIndex + 1;
    }

    return result;
  }

  /**
   * @return the config
   */
  public XlsxConfigRegistry getConfig() {
    return config;
  }

  /**
   * @return the worksheet
   */
  private XSSFSheet getWorksheet() {
    return worksheet;
  }

  @Override
  public int getSheetIndex() {
    return sheetIndex;
  }

  @Override
  public String getSheetName() {
    return getWorksheet().getSheetName();
  }

  /**
   * @return the active
   */
  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public void close() throws IOException {
    // Meh
  }
}
