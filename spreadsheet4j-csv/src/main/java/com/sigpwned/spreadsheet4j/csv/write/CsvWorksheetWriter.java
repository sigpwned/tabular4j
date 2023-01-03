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
import java.util.List;
import com.sigpwned.csv4j.CsvField;
import com.sigpwned.csv4j.CsvRecord;
import com.sigpwned.csv4j.write.CsvWriter;
import com.sigpwned.spreadsheet4j.csv.CsvConfigRegistry;
import com.sigpwned.spreadsheet4j.csv.util.Csv;
import com.sigpwned.spreadsheet4j.io.ByteSink;
import com.sigpwned.spreadsheet4j.io.CharSink;
import com.sigpwned.spreadsheet4j.model.WorksheetCellDefinition;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class CsvWorksheetWriter implements WorksheetWriter {
  private final CsvConfigRegistry config;
  private final CharSink sink;
  private CsvWriter delegate;

  public CsvWorksheetWriter(CsvConfigRegistry config, ByteSink sink) {
    this(config, sink.asCharSink(StandardCharsets.UTF_8));
  }

  public CsvWorksheetWriter(CsvConfigRegistry config, CharSink sink) {
    this.config = requireNonNull(config);
    this.sink = requireNonNull(sink);
  }

  @Override
  public int getSheetIndex() {
    return 0;
  }

  @Override
  public String getSheetName() {
    return Csv.WORKSHEET_NAME;
  }

  @Override
  public void writeRow(List<WorksheetCellDefinition> cells) throws IOException {
    if (delegate == null)
      delegate = new CsvWriter(getSink().getWriter());

    List<CsvField> fields = cells.stream().map(cell -> {
      CsvField field = new CsvField();
      if (cell != null && cell.getValue() != null) {
        getConfig().findValueMapperForType(cell.getValue().getType())
            .orElseThrow(() -> new IllegalStateException("no mapper for type"))
            .setValue(field, cell.getValue().getValue());
      } else {
        field = field.withQuoted(false).withText("");
      }
      return field;
    }).toList();

    // We don't care about style at all
    delegate.writeNext(CsvRecord.of(fields));
  }

  @Override
  public void close() throws IOException {
    if (delegate != null)
      delegate.close();
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
