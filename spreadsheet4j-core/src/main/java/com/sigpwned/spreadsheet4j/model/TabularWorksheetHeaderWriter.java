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
package com.sigpwned.spreadsheet4j.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.sigpwned.spreadsheet4j.filter.RectangularWorksheetWriter;

public class TabularWorksheetHeaderWriter {
  private final WorksheetWriter delegate;

  public TabularWorksheetHeaderWriter(WorksheetWriter delegate) {
    this.delegate = new RectangularWorksheetWriter(delegate);
  }

  public int getSheetIndex() {
    return getDelegate().getSheetIndex();
  }

  public String getSheetName() {
    return getDelegate().getSheetName();
  }

  public TabularWorksheetRowWriter writeHeaders(String... headers) throws IOException {
    return writeHeaders(Arrays.asList(headers));
  }

  public TabularWorksheetRowWriter writeHeaders(List<String> headers) throws IOException {
    if (headers == null)
      throw new NullPointerException();
    if (headers.isEmpty())
      throw new IllegalArgumentException("no headers");
    getDelegate().writeRow(headers.stream().map(WorksheetCellDefinition::ofValue).toList());
    return new TabularWorksheetRowWriter(getDelegate(), headers);
  }

  /**
   * @return the delegate
   */
  private WorksheetWriter getDelegate() {
    return delegate;
  }
}
