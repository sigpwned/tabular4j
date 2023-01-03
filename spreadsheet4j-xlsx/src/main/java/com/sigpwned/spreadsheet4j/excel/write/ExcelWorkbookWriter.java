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
package com.sigpwned.spreadsheet4j.excel.write;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.sigpwned.spreadsheet4j.excel.ExcelConfigRegistry;
import com.sigpwned.spreadsheet4j.model.WorkbookWriter;

public class ExcelWorkbookWriter implements WorkbookWriter {
  private final ExcelConfigRegistry config;
  private final Workbook workbook;

  public ExcelWorkbookWriter(ExcelConfigRegistry config, Workbook workbook) {
    this.config = requireNonNull(config);
    this.workbook = requireNonNull(workbook);
  }

  @Override
  public ExcelWorksheetWriter getWorksheet(String sheetName) {
    Sheet sheet = getWorkbook().createSheet(sheetName);
    int sheetIndex = getWorkbook().getSheetIndex(sheetName);
    return new ExcelWorksheetWriter(getConfig(), sheet, sheetIndex);
  }

  @Override
  public void close() throws IOException {
    getWorkbook().close();
  }

  /**
   * @return the workbook
   */
  private Workbook getWorkbook() {
    return workbook;
  }

  /**
   * @return the config
   */
  private ExcelConfigRegistry getConfig() {
    return config;
  }
}
