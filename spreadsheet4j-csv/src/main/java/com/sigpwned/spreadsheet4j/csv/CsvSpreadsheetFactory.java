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
package com.sigpwned.spreadsheet4j.csv;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.sigpwned.spreadsheet4j.SpreadsheetFactory;
import com.sigpwned.spreadsheet4j.csv.read.CsvWorkbookReader;
import com.sigpwned.spreadsheet4j.csv.read.CsvWorksheetReader;
import com.sigpwned.spreadsheet4j.csv.write.CsvWorkbookWriter;
import com.sigpwned.spreadsheet4j.csv.write.CsvWorksheetWriter;
import com.sigpwned.spreadsheet4j.io.ByteSink;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.io.CharSink;
import com.sigpwned.spreadsheet4j.io.CharSource;
import com.sigpwned.spreadsheet4j.util.MoreChardet;

public class CsvSpreadsheetFactory implements SpreadsheetFactory {
  private final CsvConfigRegistry config;

  public CsvSpreadsheetFactory() {
    this(new CsvConfigRegistry());
  }

  public CsvSpreadsheetFactory(CsvConfigRegistry config) {
    this.config = requireNonNull(config);
  }

  @Override
  public CsvWorkbookReader readWorkbook(ByteSource source) throws IOException {
    return readWorkbook(MoreChardet.decode(source));
  }

  public CsvWorkbookReader readWorkbook(CharSource source) throws IOException {
    return new CsvWorkbookReader(getConfig(), source);
  }

  @Override
  public CsvWorksheetReader readActiveWorksheet(ByteSource source) throws IOException {
    return readActiveWorksheet(MoreChardet.decode(source));
  }

  public CsvWorksheetReader readActiveWorksheet(CharSource source) throws IOException {
    return new CsvWorksheetReader(getConfig(), source);
  }

  @Override
  public CsvWorkbookWriter writeWorkbook(ByteSink sink) throws IOException {
    return writeWorkbook(sink.asCharSink(StandardCharsets.UTF_8));
  }

  public CsvWorkbookWriter writeWorkbook(CharSink sink) throws IOException {
    return new CsvWorkbookWriter(getConfig(), sink);
  }

  @Override
  public CsvWorksheetWriter writeActiveWorksheet(ByteSink sink) throws IOException {
    return writeActiveWorksheet(sink.asCharSink(StandardCharsets.UTF_8));
  }

  public CsvWorksheetWriter writeActiveWorksheet(CharSink sink) throws IOException {
    return new CsvWorksheetWriter(getConfig(), sink);
  }

  /**
   * @return the config
   */
  public CsvConfigRegistry getConfig() {
    return config;
  }
}
