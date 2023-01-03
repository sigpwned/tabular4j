/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-csv
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
package com.sigpwned.spreadsheet4j.csv.read;

import java.io.IOException;
import java.util.List;
import com.sigpwned.spreadsheet4j.csv.CsvConfigRegistry;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.io.CharSource;
import com.sigpwned.spreadsheet4j.model.WorkbookReader;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.util.MoreChardet;

public class CsvWorkbookReader implements WorkbookReader {
  private final CsvConfigRegistry config;
  private final CharSource source;

  public CsvWorkbookReader(CsvConfigRegistry config, ByteSource source) throws IOException {
    this(config, MoreChardet.decode(source));
  }

  public CsvWorkbookReader(CsvConfigRegistry config, CharSource source) throws IOException {
    this.config = config;
    this.source = source;
  }

  @Override
  public List<WorksheetReader> getWorksheets() {
    return List.<WorksheetReader>of(new CsvWorksheetReader(getConfig(), getSource()));
  }

  @Override
  public void close() throws IOException {
    // NOP
  }

  /**
   * @return the config
   */
  public CsvConfigRegistry getConfig() {
    return config;
  }

  /**
   * @return the source
   */
  private CharSource getSource() {
    return source;
  }
}
