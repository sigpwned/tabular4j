/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
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
package com.sigpwned.tabular4j.model;

import static java.util.Collections.unmodifiableList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TabularWorksheetRow implements Iterable<TabularWorksheetCell> {
  private final int rowIndex;
  private final List<TabularWorksheetCell> cells;

  public TabularWorksheetRow(int rowIndex, List<TabularWorksheetCell> cells) {
    this.rowIndex = rowIndex;
    this.cells = unmodifiableList(cells);
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public List<TabularWorksheetCell> getCells() {
    return cells;
  }

  public TabularWorksheetCell getCell(int index) {
    return getCells().get(index);
  }

  public Optional<TabularWorksheetCell> findCellByColumnName(String columnName) {
    return stream().filter(c -> c.getColumnName().equals(columnName)).findFirst();
  }

  public int size() {
    return getCells().size();
  }

  public Iterator<TabularWorksheetCell> iterator() {
    return getCells().iterator();
  }

  public Stream<TabularWorksheetCell> stream() {
    return getCells().stream();
  }
}
