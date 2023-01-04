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

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import com.sigpwned.spreadsheet4j.model.WorkbookReader;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;

public class ForwardingWorkbookReader implements WorkbookReader {
  private final WorkbookReader delegate;

  public ForwardingWorkbookReader(WorkbookReader delegate) {
    this.delegate = requireNonNull(delegate);
  }

  /**
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorkbookReader#getWorksheetCount()
   */
  public int getWorksheetCount() {
    return getDelegate().getWorksheetCount();
  }


  /**
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorkbookReader#getWorksheetNames()
   */
  public List<String> getWorksheetNames() {
    return getDelegate().getWorksheetNames();
  }


  /**
   * @param index
   * @return
   * @throws IOException
   * @see com.sigpwned.spreadsheet4j.model.WorkbookReader#getWorksheet(int)
   */
  public WorksheetReader getWorksheet(int index) throws IOException {
    return getDelegate().getWorksheet(index);
  }


  /**
   * @param name
   * @return
   * @throws IOException
   * @see com.sigpwned.spreadsheet4j.model.WorkbookReader#findWorksheetByName(java.lang.String)
   */
  public Optional<WorksheetReader> findWorksheetByName(String name) throws IOException {
    return getDelegate().findWorksheetByName(name);
  }

  /**
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorkbookReader#getActiveWorksheetIndex()
   */
  public int getActiveWorksheetIndex() {
    return getDelegate().getActiveWorksheetIndex();
  }

  @Override
  public WorksheetReader getActiveWorksheet() throws IOException {
    return getDelegate().getActiveWorksheet();
  }

  @Override
  public void close() throws IOException {
    getDelegate().close();
  }

  /**
   * @return the delegate
   */
  private WorkbookReader getDelegate() {
    return delegate;
  }
}
