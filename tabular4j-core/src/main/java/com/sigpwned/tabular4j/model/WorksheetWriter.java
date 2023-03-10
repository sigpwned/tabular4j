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

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import java.io.IOException;
import java.util.List;

public interface WorksheetWriter extends AutoCloseable {
  public int getSheetIndex();

  public String getSheetName();

  public default void writeValuesRow(Object... cells) throws IOException {
    writeValuesRow(asList(cells));
  }

  public default void writeValuesRow(List<Object> cells) throws IOException {
    writeRow(cells.stream().map(WorksheetCellDefinition::ofValue).collect(toList()));
  }

  public void writeRow(List<WorksheetCellDefinition> cells) throws IOException;

  @Override
  public void close() throws IOException;
}
