/*-
 * =================================LICENSE_START==================================
 * tabular4j-core
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
package com.sigpwned.tabular4j.test;

import static java.util.stream.Collectors.toList;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.sigpwned.tabular4j.model.WorksheetCellDefinition;
import com.sigpwned.tabular4j.model.WorksheetCellValue;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public class TestWorksheetWriter implements WorksheetWriter {
  private final List<List<String>> rows;

  public TestWorksheetWriter(List<List<String>> rows) {
    this.rows = rows;
  }

  @Override
  public int getSheetIndex() {
    return 0;
  }

  @Override
  public String getSheetName() {
    return "test";
  }

  @Override
  public void writeRow(List<WorksheetCellDefinition> cells) throws IOException {
    rows.add(cells.stream().map(c -> Optional.ofNullable(c.getValue())
        .map(WorksheetCellValue::getValue).map(Objects::toString).orElse("")).collect(toList()));
  }

  @Override
  public void close() throws IOException {
    // NOP
  }
}
