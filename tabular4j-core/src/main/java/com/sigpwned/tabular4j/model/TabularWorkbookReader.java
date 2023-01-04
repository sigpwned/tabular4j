/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
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

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class TabularWorkbookReader implements AutoCloseable {
  private final WorkbookReader delegate;

  public TabularWorkbookReader(WorkbookReader delegate) {
    this.delegate = requireNonNull(delegate);
  }

  public int getWorksheetCount() {
    return getDelegate().getWorksheetCount();
  }

  public List<String> getWorksheetNames() {
    return getDelegate().getWorksheetNames();
  }

  public TabularWorksheetReader getWorksheet(int index) throws IOException {
    return new TabularWorksheetReader(getDelegate().getWorksheet(index));
  }

  public Optional<TabularWorksheetReader> findWorksheetByName(String name) throws IOException {
    try {
      return IntStream.range(0, getWorksheetCount())
          .filter(i -> getWorksheetNames().get(i).equals(name)).mapToObj(i -> {
            try {
              return getWorksheet(i);
            } catch (IOException e) {
              throw new UncheckedIOException(e);
            }
          }).findFirst();
    } catch (UncheckedIOException e) {
      throw (IOException) e.getCause();
    }
  }

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
