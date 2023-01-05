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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public interface WorkbookReader extends AutoCloseable {
  public int getWorksheetCount();

  public List<String> getWorksheetNames();

  public int getActiveWorksheetIndex();

  public WorksheetReader getWorksheet(int index) throws IOException;

  public default Optional<WorksheetReader> findWorksheetByName(String name) throws IOException {
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
      throw e.getCause();
    }
  }

  public default WorksheetReader getActiveWorksheet() throws IOException {
    return getWorksheet(getActiveWorksheetIndex());
  }

  public void close() throws IOException;
}
