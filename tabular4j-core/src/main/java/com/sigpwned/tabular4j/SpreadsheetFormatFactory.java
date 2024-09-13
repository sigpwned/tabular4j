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
import com.sigpwned.tabular4j.exception.UnrecognizedFormatSpreadsheetException;
import com.sigpwned.tabular4j.io.ByteSink;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.io.SpreadsheetSink;
import com.sigpwned.tabular4j.io.SpreadsheetSource;
import com.sigpwned.tabular4j.mime.MimeType;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorkbookWriter;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public interface SpreadsheetFormatFactory {
  public static final int DEFAULT_PRIORITY = 10000;

  /**
   * Get the priority of this format. Formats with higher priority are tried first when reading
   * files. This method must always return the same value. The default priority is
   * {@value #DEFAULT_PRIORITY}.
   * 
   * @return the priority
   */
  public default int getPriority() {
    return DEFAULT_PRIORITY;
  }

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
   * Read a workbook from the given source. Implementations are encouraged to use the given
   * {@link SpreadsheetSource source}'s {@link SpreadsheetSource#getFileExtension() file extension}
   * and {@link SpreadsheetSource#getMimeType() MIME type} to determine support quickly, and to
   * inform worksheet attributes, e.g., character encoding for text-based formats. Most formats will
   * delegate to {@link #readWorkbook(ByteSource)} after such checks. Users may use
   * {@link #readWorkbook(ByteSource)} directly if preferred.
   * 
   * @param source
   * @return the workbook reader
   * @throws IOException if an I/O error occurs
   * @throws InvalidFileSpreadsheetException if the given source does not contain a valid workbook
   *         of this format, either due to an unsupported file extension or MIME type, or due to an
   *         invalid file format
   */
  public default WorkbookReader readWorkbook(SpreadsheetSource source) throws IOException {
    return readWorkbook(source.getBytes());
  }

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

  /**
   * Read the active worksheet from the workbook in the given source. Implementations are encouraged
   * to use the given {@link SpreadsheetSource source}'s {@link SpreadsheetSource#getFileExtension()
   * file extension} and {@link SpreadsheetSource#getMimeType() MIME type} to determine support
   * quickly, and to inform worksheet attributes, e.g., character encoding for text-based formats.
   * Most formats will delegate to {@link #readActiveWorksheet(ByteSource)} after such checks. Users
   * may use {@link #readActiveWorksheet(ByteSource)} directly if preferred.
   * 
   * @param source
   * @return the worksheet reader
   * @throws IOException if an I/O error occurs
   * @throws InvalidFileSpreadsheetException if the given source does not contain a valid workbook
   *         of this format, either due to an unsupported file extension or MIME type, or due to an
   *         invalid file format
   */
  public default WorksheetReader readActiveWorksheet(SpreadsheetSource source) throws IOException {
    return readActiveWorksheet(source.getBytes());
  }

  public default WorkbookWriter writeWorkbook(SpreadsheetSink sink) throws IOException {
    if (!sink.getFileExtension().equals(getDefaultFileExtension()))
      throw new UnrecognizedFormatSpreadsheetException(sink.getFileExtension());
    return writeWorkbook(sink.getBytes());
  }

  public WorkbookWriter writeWorkbook(ByteSink sink) throws IOException;

  public default WorksheetWriter writeActiveWorksheet(SpreadsheetSink sink) throws IOException {
    if (!sink.getFileExtension().equals(getDefaultFileExtension()))
      throw new UnrecognizedFormatSpreadsheetException(sink.getFileExtension());
    return writeActiveWorksheet(sink.getBytes());
  }

  public WorksheetWriter writeActiveWorksheet(ByteSink sink) throws IOException;

  /**
   * Get the default file extension for this format. Used when writing files.
   */
  public String getDefaultFileExtension();

  /**
   * Get the default MIME type for this format. Used when writing files.
   */
  public MimeType getDefaultMimeType();
}
