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
package com.sigpwned.spreadsheet4j.model;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface WorkbookReader extends AutoCloseable {
  /**
   * @return the worksheets
   */
  public List<WorksheetReader> getWorksheets();

  public default WorksheetReader getWorksheetByIndex(int index) {
    return getWorksheets().get(index);
  }

  public default Optional<WorksheetReader> findWorksheetByName(String name) {
    return getWorksheets().stream().filter(s -> s.getSheetName().equals(name)).findFirst();
  }

  public default WorksheetReader getActiveWorksheet() {
    return getWorksheets().stream().filter(WorksheetReader::isActive).findFirst().get();
  }

  public void close() throws IOException;
}
