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
package com.sigpwned.spreadsheet4j.forwarding;

import java.io.IOException;
import com.sigpwned.spreadsheet4j.model.WorkbookWriter;
import com.sigpwned.spreadsheet4j.model.WorksheetCellStyle;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class ForwardingWorkbookWriter implements WorkbookWriter {
  private final WorkbookWriter delegate;

  public ForwardingWorkbookWriter(WorkbookWriter delegate) {
    this.delegate = delegate;
  }

  /**
   * @param bold
   * @param italic
   * @param underlined
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorksheetCellStyleFactory#getStyle(boolean, boolean,
   *      boolean)
   */
  public WorksheetCellStyle getStyle(boolean bold, boolean italic, boolean underlined) {
    return getDelegate().getStyle(bold, italic, underlined);
  }

  /**
   * @param name
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorkbookWriter#getWorksheet(java.lang.String)
   */
  public WorksheetWriter getWorksheet(String name) {
    return getDelegate().getWorksheet(name);
  }

  /**
   * @throws IOException
   * @see com.sigpwned.spreadsheet4j.model.WorkbookWriter#close()
   */
  public void close() throws IOException {
    getDelegate().close();
  }

  private WorkbookWriter getDelegate() {
    return delegate;
  }
}
