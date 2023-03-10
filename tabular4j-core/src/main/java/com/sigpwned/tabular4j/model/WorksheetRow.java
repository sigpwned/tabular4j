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

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public interface WorksheetRow extends Iterable<WorksheetCell> {
  public int getRowIndex();

  public List<WorksheetCell> getCells();

  public default int getCellCount() {
    return getCells().size();
  }

  public default WorksheetCell getCell(int index) {
    return getCells().get(index);
  }

  public default int size() {
    return getCells().size();
  }

  public default Iterator<WorksheetCell> iterator() {
    return getCells().iterator();
  }

  public default Stream<WorksheetCell> stream() {
    return getCells().stream();
  }
}
