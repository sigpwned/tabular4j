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
package com.sigpwned.tabular4j.excel;

import static java.util.Objects.requireNonNull;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import com.sigpwned.tabular4j.model.WorksheetCell;
import com.sigpwned.tabular4j.type.QualifiedType;

public class ExcelWorksheetCell implements WorksheetCell {
  private final ExcelConfigRegistry config;
  private final int columnIndex;

  /**
   * Excel does not materialize all "logical" cells. (For example, Excel might only store data for
   * cell 3 on line 5.) This value will be {@code null} for such non-materialized cells. (In the
   * previous example, the {@code WorksheetCell} instances for cells 0, 1, 2 on line 5 would have a
   * null value for this property.)
   */
  private final Cell cell;

  public ExcelWorksheetCell(ExcelConfigRegistry config, int columnIndex, Cell cell) {
    this.config = requireNonNull(config);
    this.columnIndex = columnIndex;
    this.cell = cell;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getValue(QualifiedType<T> type) {
    if (getCell() == null)
      return null;
    if (getCell().getCellType() == CellType.BLANK)
      return null;
    return (T) getConfig().findValueMapperForType(type).map(v -> v.getValue(getCell()))
        .orElseThrow(() -> new IllegalStateException("no mapper for type"));
  }

  /**
   * @return the config
   */
  public ExcelConfigRegistry getConfig() {
    return config;
  }

  @Override
  public int getColumnIndex() {
    return columnIndex;
  }

  /**
   * @return the cell
   */
  private Cell getCell() {
    return cell;
  }
}
