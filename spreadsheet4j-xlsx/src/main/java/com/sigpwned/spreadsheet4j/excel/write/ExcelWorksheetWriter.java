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
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import com.sigpwned.spreadsheet4j.excel.ExcelConfigRegistry;
import com.sigpwned.spreadsheet4j.model.WorksheetCellDefinition;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class ExcelWorksheetWriter implements WorksheetWriter {
  private final ExcelConfigRegistry config;
  private final Sheet worksheet;
  private final int sheetIndex;
  private int rowIndex;

  public ExcelWorksheetWriter(ExcelConfigRegistry config, Sheet worksheet, int sheetIndex) {
    this.config = requireNonNull(config);
    this.worksheet = requireNonNull(worksheet);
    this.sheetIndex = sheetIndex;
  }

  @Override
  public int getSheetIndex() {
    return sheetIndex;
  }

  @Override
  public String getSheetName() {
    return getWorksheet().getSheetName();
  }

  @Override
  public void writeRow(List<WorksheetCellDefinition> cells) throws IOException {
    Row row = getWorksheet().createRow(rowIndex++);
    for (int i = 0; i < cells.size(); i++) {
      WorksheetCellDefinition ci = cells.get(i);

      if (ci.getValue() == null)
        continue;

      Cell cell = row.createCell(i);

      getConfig().findValueMapperForType(ci.getValue().getType())
          .orElseThrow(() -> new IllegalStateException("no mapper for type"))
          .setValue(cell, ci.getValue().getValue());
    }
  }

  @Override
  public void close() throws IOException {
    // Meh
  }

  /**
   * @return the config
   */
  public ExcelConfigRegistry getConfig() {
    return config;
  }

  /**
   * @return the worksheet
   */
  private Sheet getWorksheet() {
    return worksheet;
  }
}
