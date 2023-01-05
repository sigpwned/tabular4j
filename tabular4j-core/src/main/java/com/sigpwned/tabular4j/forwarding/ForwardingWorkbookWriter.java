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

import java.io.IOException;
import com.sigpwned.tabular4j.model.WorkbookWriter;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public class ForwardingWorkbookWriter implements WorkbookWriter {
  private final WorkbookWriter delegate;

  public ForwardingWorkbookWriter(WorkbookWriter delegate) {
    this.delegate = delegate;
  }

  /**
   * @param name
   * @return
   * @throws IOException
   * @see com.sigpwned.tabular4j.model.WorkbookWriter#getWorksheet(java.lang.String)
   */
  @Override
  public WorksheetWriter getWorksheet(String name) throws IOException {
    return getDelegate().getWorksheet(name);
  }

  /**
   * @throws IOException
   * @see com.sigpwned.tabular4j.model.WorkbookWriter#close()
   */
  @Override
  public void close() throws IOException {
    getDelegate().close();
  }

  private WorkbookWriter getDelegate() {
    return delegate;
  }
}
