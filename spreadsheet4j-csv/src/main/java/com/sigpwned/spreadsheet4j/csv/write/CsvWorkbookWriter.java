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
package com.sigpwned.spreadsheet4j.csv.write;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.sigpwned.spreadsheet4j.csv.CsvConfigRegistry;
import com.sigpwned.spreadsheet4j.io.ByteSink;
import com.sigpwned.spreadsheet4j.io.CharSink;
import com.sigpwned.spreadsheet4j.model.WorkbookWriter;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class CsvWorkbookWriter implements WorkbookWriter {
  private final CsvConfigRegistry config;
  private final CharSink sink;

  public CsvWorkbookWriter(CsvConfigRegistry config, ByteSink sink) {
    this(config, sink.asCharSink(StandardCharsets.UTF_8));
  }

  public CsvWorkbookWriter(CsvConfigRegistry config, CharSink sink) {
    this.config = requireNonNull(config);
    this.sink = requireNonNull(sink);
  }

  @Override
  public WorksheetWriter getWorksheet(String name) {
    // We don't care about the name.
    return new CsvWorksheetWriter(getConfig(), getSink());
  }

  @Override
  public void close() throws IOException {
    // Nothing to do.
  }

  /**
   * @return the config
   */
  public CsvConfigRegistry getConfig() {
    return config;
  }

  /**
   * @return the sink
   */
  private CharSink getSink() {
    return sink;
  }
}
