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

import static java.util.Collections.unmodifiableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import com.sigpwned.tabular4j.exception.InvalidFileSpreadsheetException;
import com.sigpwned.tabular4j.exception.UnrecognizedFormatSpreadsheetException;
import com.sigpwned.tabular4j.io.ByteSink;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.io.SpreadsheetSink;
import com.sigpwned.tabular4j.io.SpreadsheetSource;
import com.sigpwned.tabular4j.model.TabularWorkbookReader;
import com.sigpwned.tabular4j.model.TabularWorkbookWriter;
import com.sigpwned.tabular4j.model.TabularWorksheetHeaderWriter;
import com.sigpwned.tabular4j.model.TabularWorksheetReader;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorkbookWriter;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public class SpreadsheetFactory {
  private static final SpreadsheetFactory DEFAULT_INSTANCE = new SpreadsheetFactory();

  public static SpreadsheetFactory getInstance() {
    return DEFAULT_INSTANCE;
  }

  private final List<SpreadsheetFormatFactory> formats;

  public SpreadsheetFactory() {
    this.formats = new ArrayList<>();
    ServiceLoader.load(SpreadsheetFormatFactory.class).forEach(this::register);
  }

  /**
   * @return the formats
   */
  public List<SpreadsheetFormatFactory> getFormats() {
    return unmodifiableList(formats);
  }

  /**
   * Add a new {@link SpreadsheetFormatFactory format} to the list of known formats. The format is
   * added to the list in order of its {@link SpreadsheetFormatFactory#getPriority() priority}. If
   * two formats have the same priority, then the relative order of the formats is not specified.
   */
  public void register(SpreadsheetFormatFactory format) {
    formats.add(format);
    formats.sort((a, b) -> -Integer.compare(a.getPriority(), b.getPriority()));
  }

  public Optional<SpreadsheetFormatFactory> findFormatByDefaultFileExtension(
      String defaultFileExtension) {
    return getFormats().stream()
        .filter(f -> f.getDefaultFileExtension().equals(defaultFileExtension)).findFirst();
  }

  public WorkbookReader readWorkbook(SpreadsheetSource source) throws IOException {
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        return format.readWorkbook(source);
      } catch (InvalidFileSpreadsheetException e) {
        // This is OK. It's just not in the given format.
      }
    }
    if (source.getMimeType().isEmpty() && source.getFileExtension().isEmpty())
      throw new InvalidFileSpreadsheetException();
    return readWorkbook(source.getBytes());
  }

  public WorkbookReader readWorkbook(ByteSource source) throws IOException {
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        return format.readWorkbook(source);
      } catch (InvalidFileSpreadsheetException e) {
        // This is OK. It's just not in the given format.
      }
    }
    throw new InvalidFileSpreadsheetException();
  }

  public WorksheetReader readActiveWorksheet(SpreadsheetSource source) throws IOException {
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        return format.readActiveWorksheet(source);
      } catch (InvalidFileSpreadsheetException e) {
        // This is OK. It's just not in the given format.
      }
    }
    if (source.getMimeType().isEmpty() && source.getFileExtension().isEmpty())
      throw new InvalidFileSpreadsheetException();
    return readActiveWorksheet(source.getBytes());
  }

  public WorksheetReader readActiveWorksheet(ByteSource source) throws IOException {
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        return format.readActiveWorksheet(source);
      } catch (InvalidFileSpreadsheetException e) {
        // This is OK. It's just not in the given format.
      }
    }
    throw new InvalidFileSpreadsheetException();
  }

  public WorkbookWriter writeWorkbook(SpreadsheetSink sink) throws IOException {
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        return format.writeWorkbook(sink);
      } catch (UnrecognizedFormatSpreadsheetException e) {
        // This is OK. It's just not in the given format.
      }
    }
    if (sink.getMimeType().isEmpty())
      throw new UnrecognizedFormatSpreadsheetException(sink.getFileExtension());
    return writeWorkbook(sink.getBytes(), sink.getFileExtension());
  }

  public WorkbookWriter writeWorkbook(ByteSink sink, String fileExtension) throws IOException {
    if (fileExtension == null)
      throw new NullPointerException();
    return findFormatByDefaultFileExtension(fileExtension)
        .orElseThrow(() -> new UnrecognizedFormatSpreadsheetException(fileExtension))
        .writeWorkbook(sink);
  }

  public WorksheetWriter writeActiveWorksheet(SpreadsheetSink sink) throws IOException {
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        return format.writeActiveWorksheet(sink);
      } catch (UnrecognizedFormatSpreadsheetException e) {
        // This is OK. It's just not in the given format.
      }
    }
    if (sink.getMimeType().isEmpty())
      throw new UnrecognizedFormatSpreadsheetException(sink.getFileExtension());
    return writeActiveWorksheet(sink.getBytes(), sink.getFileExtension());
  }

  public WorksheetWriter writeActiveWorksheet(ByteSink sink, String fileExtension)
      throws IOException {
    if (fileExtension == null)
      throw new NullPointerException();
    return findFormatByDefaultFileExtension(fileExtension)
        .orElseThrow(() -> new UnrecognizedFormatSpreadsheetException(fileExtension))
        .writeActiveWorksheet(sink);
  }

  public TabularWorkbookReader readTabularWorkbook(SpreadsheetSource source) throws IOException {
    return new TabularWorkbookReader(readWorkbook(source));
  }

  public TabularWorkbookReader readTabularWorkbook(ByteSource source) throws IOException {
    return new TabularWorkbookReader(readWorkbook(source));
  }

  public TabularWorksheetReader readActiveTabularWorksheet(SpreadsheetSource source)
      throws IOException {
    return new TabularWorksheetReader(readActiveWorksheet(source));
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
