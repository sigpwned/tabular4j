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
package com.sigpwned.tabular4j.csv;

import static java.util.Collections.unmodifiableList;
import java.util.List;
import com.sigpwned.tabular4j.model.WorksheetCell;
import com.sigpwned.tabular4j.model.WorksheetRow;

public class CsvWorksheetRow implements WorksheetRow {
  private final int rowIndex;
  private final List<CsvWorksheetCell> cells;

  public CsvWorksheetRow(int rowIndex, List<CsvWorksheetCell> cells) {
    if (rowIndex < 0)
      throw new IllegalArgumentException("rowIndex must not be negative");
    if (cells == null)
      throw new NullPointerException();
    this.rowIndex = rowIndex;
    this.cells = unmodifiableList(cells);

  }

  @Override
  public int getRowIndex() {
    return rowIndex;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<WorksheetCell> getCells() {
    return (List) cells;
  }
}
