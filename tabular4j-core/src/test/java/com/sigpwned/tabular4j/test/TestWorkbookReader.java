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
import java.io.IOException;
import java.util.List;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorksheetReader;

public class TestWorkbookReader implements WorkbookReader {
  private final List<List<String>> records;

  public TestWorkbookReader(List<List<String>> records) {
    this.records = unmodifiableList(records);
  }

  @Override
  public int getWorksheetCount() {
    return 1;
  }

  @Override
  public List<String> getWorksheetNames() {
    return List.of("test");
  }

  @Override
  public int getActiveWorksheetIndex() {
    return 0;
  }

  @Override
  public WorksheetReader getWorksheet(int index) throws IOException {
    return new TestWorksheetReader(records.listIterator());
  }

  @Override
  public void close() throws IOException {
    // NOP
  }
}
