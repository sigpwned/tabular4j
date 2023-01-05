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

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.sigpwned.tabular4j.io.ByteSink;
import com.sigpwned.tabular4j.model.WorkbookWriter;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public class TestWorkbookWriter implements WorkbookWriter {
  private final ByteSink sink;
  private final List<List<String>> rows;

  public TestWorkbookWriter(ByteSink sink) {
    this.sink = requireNonNull(sink);
    this.rows = new ArrayList<>();
  }

  @Override
  public WorksheetWriter getWorksheet(String name) throws IOException {
    rows.clear();
    return new TestWorksheetWriter(rows);
  }

  public List<List<String>> rows() {
    return rows.stream().map(xs -> unmodifiableList(xs)).collect(toUnmodifiableList());
  }

  @Override
  public void close() throws IOException {
    TestSpreadsheetFormatFactory.write(sink, rows);
  }
}
