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
package com.sigpwned.spreadsheet4j;

import static java.util.Collections.unmodifiableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.sigpwned.spreadsheet4j.io.ByteSink;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.model.TabularWorkbookReader;
import com.sigpwned.spreadsheet4j.model.TabularWorkbookWriter;
import com.sigpwned.spreadsheet4j.model.TabularWorksheetHeaderWriter;
import com.sigpwned.spreadsheet4j.model.TabularWorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorkbookReader;
import com.sigpwned.spreadsheet4j.model.WorkbookWriter;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class SpreadsheetFactory {
  private static final SpreadsheetFactory DEFAULT_INSTANCE = new SpreadsheetFactory();

  public static SpreadsheetFactory getInstance() {
    return DEFAULT_INSTANCE;
  }

  private final List<SpreadsheetFormatFactory> formats;

  public SpreadsheetFactory() {
    this.formats = new ArrayList<>();
  }

  /**
   * @return the formats
   */
  public List<SpreadsheetFormatFactory> getFormats() {
    return unmodifiableList(formats);
  }

  public void addFormat(SpreadsheetFormatFactory format) {
    getFormats().add(format);
  }

  public Optional<SpreadsheetFormatFactory> findFormatByDefaultFileExtension(
      String defaultFileExtension) {
    return getFormats().stream()
        .filter(f -> f.getDefaultFileExtension().equals(defaultFileExtension)).findFirst();
  }

  public WorkbookReader readWorkbook(ByteSource source) throws IOException {
    WorkbookReader result = null;
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        result = format.readWorkbook(source);
      } catch (IOException e) {
        // TODO Should we only catch a specific format mismatch exception here?
        // This is OK. It's just not in the given format.
      }
    }
    if (result == null)
      throw new IOException("Unrecognized file format");
    return result;
  }

  public WorksheetReader readActiveWorksheet(ByteSource source) throws IOException {
    WorksheetReader result = null;
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        result = format.readActiveWorksheet(source);
      } catch (IOException e) {
        // TODO Should we only catch a specific format mismatch exception here?
        // This is OK. It's just not in the given format.
      }
    }
    if (result == null)
      throw new IOException("Unrecognized file format");
    return result;
  }

  public WorkbookWriter writeWorkbook(ByteSink sink, String fileExtension) throws IOException {
    return this.findFormatByDefaultFileExtension(fileExtension)
        .orElseThrow(() -> new IOException("Unrecognized file format")).writeWorkbook(sink);
  }

  public WorksheetWriter writeActiveWorksheet(ByteSink sink, String fileExtension)
      throws IOException {
    return this.findFormatByDefaultFileExtension(fileExtension)
        .orElseThrow(() -> new IOException("Unrecognized file format")).writeActiveWorksheet(sink);
  }

  public TabularWorkbookReader readTabularWorkbook(ByteSource source) throws IOException {
    return new TabularWorkbookReader(readWorkbook(source));
  }

  public TabularWorksheetReader readActiveTabularWorksheet(ByteSource source) throws IOException {
    return new TabularWorksheetReader(readActiveWorksheet(source));
  }

  public TabularWorkbookWriter writeTabularWorkbook(ByteSink sink, String fileExtension)
      throws IOException {
    return new TabularWorkbookWriter(writeWorkbook(sink, fileExtension));
  }

  public TabularWorksheetHeaderWriter writeTabularActiveWorksheet(ByteSink sink,
      String fileExtension) throws IOException {
    return new TabularWorksheetHeaderWriter(writeActiveWorksheet(sink, fileExtension));
  }
}
