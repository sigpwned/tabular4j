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
import java.util.Iterator;
import java.util.stream.Stream;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetRow;

public class ForwardingWorksheetReader implements WorksheetReader {
  private final WorksheetReader delegate;

  public ForwardingWorksheetReader(WorksheetReader delegate) {
    this.delegate = requireNonNull(delegate);
  }

  /**
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetReader#getSheetIndex()
   */
  @Override
  public int getSheetIndex() {
    return getDelegate().getSheetIndex();
  }

  /**
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetReader#getSheetName()
   */
  @Override
  public String getSheetName() {
    return getDelegate().getSheetName();
  }

  /**
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetReader#isActive()
   */
  @Override
  public boolean isActive() {
    return getDelegate().isActive();
  }

  /**
   * @return
   * @throws IOException
   * @see com.sigpwned.tabular4j.model.WorksheetReader#readRow()
   */
  @Override
  public WorksheetRow readRow() throws IOException {
    return getDelegate().readRow();
  }

  /**
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetReader#iterator()
   */
  @Override
  public Iterator<WorksheetRow> iterator() {
    return getDelegate().iterator();
  }

  /**
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetReader#stream()
   */
  @Override
  public Stream<WorksheetRow> stream() {
    return getDelegate().stream();
  }

  /**
   * @throws IOException
   * @see com.sigpwned.tabular4j.model.WorksheetReader#close()
   */
  @Override
  public void close() throws IOException {
    getDelegate().close();
  }

  private WorksheetReader getDelegate() {
    return delegate;
  }
}
