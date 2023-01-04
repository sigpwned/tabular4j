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
package com.sigpwned.tabular4j.forwarding;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.List;
import com.sigpwned.tabular4j.model.WorksheetCellDefinition;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public class ForwardingWorksheetWriter implements WorksheetWriter {
  private final WorksheetWriter delegate;

  public ForwardingWorksheetWriter(WorksheetWriter delegate) {
    this.delegate = requireNonNull(delegate);
  }

  /**
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetWriter#getSheetIndex()
   */
  public int getSheetIndex() {
    return getDelegate().getSheetIndex();
  }

  /**
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetWriter#getSheetName()
   */
  public String getSheetName() {
    return getDelegate().getSheetName();
  }

  /**
   * @param cells
   * @throws IOException
   * @see com.sigpwned.tabular4j.model.WorksheetWriter#writeRow(java.util.List)
   */
  public void writeRow(List<WorksheetCellDefinition> cells) throws IOException {
    getDelegate().writeRow(cells);
  }

  /**
   * @throws IOException
   * @see com.sigpwned.tabular4j.model.WorksheetWriter#close()
   */
  public void close() throws IOException {
    getDelegate().close();
  }

  private WorksheetWriter getDelegate() {
    return delegate;
  }
}
