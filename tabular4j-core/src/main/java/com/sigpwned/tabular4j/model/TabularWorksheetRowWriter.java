/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-core
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
package com.sigpwned.tabular4j.model;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.List;

public class TabularWorksheetRowWriter implements AutoCloseable {
  private final WorksheetWriter delegate;
  private final List<String> headers;

  public TabularWorksheetRowWriter(WorksheetWriter delegate, List<String> headers) {
    this.delegate = requireNonNull(delegate);
    this.headers = unmodifiableList(headers);
  }

  /**
   * @return the headers
   */
  public List<String> getHeaders() {
    return headers;
  }

  public int getSheetIndex() {
    return getDelegate().getSheetIndex();
  }

  public String getSheetName() {
    return getDelegate().getSheetName();
  }

  public void writeRow(List<WorksheetCellDefinition> cells) throws IOException {
    getDelegate().writeRow(cells);
  }

  public void close() throws IOException {
    getDelegate().close();
  }

  /**
   * @return the delegate
   */
  private WorksheetWriter getDelegate() {
    return delegate;
  }
}
