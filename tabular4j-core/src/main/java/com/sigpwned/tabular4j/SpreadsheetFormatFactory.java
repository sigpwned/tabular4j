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
package com.sigpwned.tabular4j;

import java.io.IOException;
import com.sigpwned.tabular4j.exception.InvalidFileSpreadsheetException;
import com.sigpwned.tabular4j.io.ByteSink;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorkbookWriter;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public interface SpreadsheetFormatFactory {
  /**
   * Read a workbook from the given source.
   * 
   * @param source the source to read from
   * @return the workbook reader
   * @throws IOException if an I/O error occurs
   * @throws InvalidFileSpreadsheetException if the given data does not contain a valid workbook of
   *         this format
   */
  public WorkbookReader readWorkbook(ByteSource source) throws IOException;

  /**
   * Read the active worksheet from the workbook in the given source.
   * 
   * @param source the source to read from
   * @return the worksheet reader
   * @throws IOException if an I/O error occurs
   * @throws InvalidFileSpreadsheetException if the given data does not contain a valid workbook of
   *         this format
   */
  public WorksheetReader readActiveWorksheet(ByteSource source) throws IOException;

  public WorkbookWriter writeWorkbook(ByteSink sink) throws IOException;

  public WorksheetWriter writeActiveWorksheet(ByteSink sink) throws IOException;

  public String getDefaultFileExtension();
}
