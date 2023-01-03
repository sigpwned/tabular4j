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
package com.sigpwned.spreadsheet4j.xlsx.write;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import com.sigpwned.spreadsheet4j.model.WorksheetCellDefinition;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;
import com.sigpwned.spreadsheet4j.xlsx.XlsxConfigRegistry;

public class XlsxWorksheetWriter implements WorksheetWriter {
  private final XlsxConfigRegistry config;
  private final XSSFSheet worksheet;
  private final int sheetIndex;
  private int rowIndex;

  public XlsxWorksheetWriter(XlsxConfigRegistry config, XSSFSheet worksheet, int sheetIndex) {
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
    XSSFRow row = getWorksheet().createRow(rowIndex++);
    for (int i = 0; i < cells.size(); i++) {
      WorksheetCellDefinition ci = cells.get(i);

      if (ci.getValue() == null)
        continue;

      XSSFCell cell = row.createCell(i);

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
  public XlsxConfigRegistry getConfig() {
    return config;
  }

  /**
   * @return the worksheet
   */
  private XSSFSheet getWorksheet() {
    return worksheet;
  }
}
