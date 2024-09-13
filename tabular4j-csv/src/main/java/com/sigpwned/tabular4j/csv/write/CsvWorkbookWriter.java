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
package com.sigpwned.tabular4j.csv.write;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.write.CsvWriter;
import com.sigpwned.tabular4j.csv.CsvConfigRegistry;
import com.sigpwned.tabular4j.io.ByteSink;
import com.sigpwned.tabular4j.io.CharSink;
import com.sigpwned.tabular4j.model.WorkbookWriter;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public class CsvWorkbookWriter implements WorkbookWriter {
  private final CsvConfigRegistry config;
  private final CharSink sink;
  private final CsvFormat format;
  private String sheetName;

  public CsvWorkbookWriter(CsvConfigRegistry config, ByteSink sink, CsvFormat format) {
    this(config, sink.asCharSink(StandardCharsets.UTF_8), format);
  }

  public CsvWorkbookWriter(CsvConfigRegistry config, CharSink sink, CsvFormat format) {
    this.config = requireNonNull(config);
    this.sink = requireNonNull(sink);
    this.format = requireNonNull(format);
  }

  @Override
  public WorksheetWriter getWorksheet(String name) throws IOException {
    if (name == null)
      throw new NullPointerException();
    if (sheetName != null && !name.equals(sheetName))
      throw new IllegalStateException("CSV workbooks only support one sheet");
    sheetName = name;
    return new CsvWorksheetWriter(getConfig(), new CsvWriter(getFormat(), getSink().getWriter()));
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

  /**
   * @return the format
   */
  public CsvFormat getFormat() {
    return format;
  }
}
