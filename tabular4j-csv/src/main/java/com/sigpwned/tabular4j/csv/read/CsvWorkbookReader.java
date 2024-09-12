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
package com.sigpwned.tabular4j.csv.read;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.read.CsvReader;
import com.sigpwned.tabular4j.csv.CsvConfigRegistry;
import com.sigpwned.tabular4j.csv.util.Csv;
import com.sigpwned.tabular4j.csv.util.MoreChardet;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.io.CharSource;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorksheetReader;

public class CsvWorkbookReader implements WorkbookReader {
  private final CsvConfigRegistry config;
  private final CharSource source;
  private final CsvFormat format;

  public CsvWorkbookReader(CsvConfigRegistry config, ByteSource source, CsvFormat format) throws IOException {
    this(config, MoreChardet.decode(source), format);
  }

  public CsvWorkbookReader(CsvConfigRegistry config, CharSource source, CsvFormat format) {
    this.config = requireNonNull(config);
    this.source = requireNonNull(source);
    this.format = requireNonNull(format);
  }

  @Override
  public List<String> getWorksheetNames() {
    return List.of(Csv.WORKSHEET_NAME);
  }

  @Override
  public int getActiveWorksheetIndex() {
    return 0;
  }

  @Override
  public int getWorksheetCount() {
    return 1;
  }

  @Override
  public WorksheetReader getWorksheet(int index) throws IOException {
    if (index != 0)
      throw new NoSuchElementException();
    return new CsvWorksheetReader(getConfig(), new CsvReader(getFormat(), getSource().getReader()));
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

  /**
   * @return the format
   */
  public CsvFormat getFormat() {
    return format;
  }
}
