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
package com.sigpwned.tabular4j.excel.read;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.ss.usermodel.Workbook;
import com.sigpwned.tabular4j.excel.ExcelConfigRegistry;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorksheetReader;

public class ExcelWorkbookReader implements WorkbookReader {
  private final ExcelConfigRegistry config;
  private final Workbook workbook;
  private final List<Sheet> visibleSheets;

  @SuppressWarnings("resource")
  public ExcelWorkbookReader(ExcelConfigRegistry config, Workbook workbook) {
    this.config = requireNonNull(config);
    this.workbook = requireNonNull(workbook);
    this.visibleSheets = IntStream.range(0, getWorkbook().getNumberOfSheets())
        .filter(i -> getWorkbook().getSheetVisibility(i) == SheetVisibility.VISIBLE)
        .mapToObj(i -> getWorkbook().getSheetAt(i)).collect(toUnmodifiableList());
    if (getVisibleSheets().isEmpty()) {
      // This should never happen because "An Excel Workbook must contain at least one visible
      // worksheet," per the Excel UI.
      throw new IllegalArgumentException("no visible sheets");
    }
  }

  /**
   * @return the config
   */
  public ExcelConfigRegistry getConfig() {
    return config;
  }

  @Override
  public List<String> getWorksheetNames() {
    return getVisibleSheets().stream().map(Sheet::getSheetName).collect(toList());
  }

  @Override
  public int getWorksheetCount() {
    return getVisibleSheets().size();
  }

  @Override
  public int getActiveWorksheetIndex() {
    List<Sheet> vss = getVisibleSheets();
    int asi = getWorkbook().getActiveSheetIndex();
    return IntStream.range(0, vss.size())
        .filter(i -> getWorkbook().getSheetIndex(vss.get(i)) == asi).findFirst().orElse(0);
  }

  @Override
  public WorksheetReader getWorksheet(int index) throws IOException {
    return new ExcelWorksheetReader(getConfig(), getVisibleSheets().get(index), index,
        index == getActiveWorksheetIndex());
  }

  @Override
  public void close() throws IOException {
    getWorkbook().close();
  }

  /**
   * @return the delegate
   */
  private Workbook getWorkbook() {
    return workbook;
  }

  private List<Sheet> getVisibleSheets() {
    return visibleSheets;
  }
}
