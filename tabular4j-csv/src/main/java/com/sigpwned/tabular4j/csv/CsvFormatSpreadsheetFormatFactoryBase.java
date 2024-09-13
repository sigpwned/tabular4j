/*-
 * =================================LICENSE_START==================================
 * tabular4j-csv
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2024 Andy Boothe
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
package com.sigpwned.tabular4j.csv;



import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Set;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.read.CsvReader;
import com.sigpwned.csv4j.write.CsvWriter;
import com.sigpwned.tabular4j.SpreadsheetFormatFactory;
import com.sigpwned.tabular4j.csv.read.CsvWorkbookReader;
import com.sigpwned.tabular4j.csv.read.CsvWorksheetReader;
import com.sigpwned.tabular4j.csv.util.Csv;
import com.sigpwned.tabular4j.csv.util.MoreChardet;
import com.sigpwned.tabular4j.csv.write.CsvWorkbookWriter;
import com.sigpwned.tabular4j.csv.write.CsvWorksheetWriter;
import com.sigpwned.tabular4j.exception.InvalidFileSpreadsheetException;
import com.sigpwned.tabular4j.exception.UnrecognizedFormatSpreadsheetException;
import com.sigpwned.tabular4j.io.ByteSink;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.io.CharSink;
import com.sigpwned.tabular4j.io.CharSource;
import com.sigpwned.tabular4j.io.SpreadsheetSink;
import com.sigpwned.tabular4j.io.SpreadsheetSource;
import com.sigpwned.tabular4j.mime.MimeType;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorkbookWriter;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public abstract class CsvFormatSpreadsheetFormatFactoryBase implements SpreadsheetFormatFactory {
  private final CsvConfigRegistry config;
  private final CsvFormat format;
  private final String defaultFileExtension;
  private final Set<String> supportedFileExtensions;
  private final MimeType defaultMimeType;
  private final Set<MimeType> supportedMimeTypes;

  protected CsvFormatSpreadsheetFormatFactoryBase(CsvConfigRegistry config, CsvFormat format,
      String defaultFileExtension, Set<String> supportedFileExtensions, MimeType defaultMimeType,
      Set<MimeType> supportedMimeTypes) {
    this.config = requireNonNull(config);
    this.format = requireNonNull(format);
    this.defaultFileExtension = requireNonNull(defaultFileExtension);
    this.supportedFileExtensions = unmodifiableSet(supportedFileExtensions);
    this.defaultMimeType = requireNonNull(defaultMimeType);
    this.supportedMimeTypes = unmodifiableSet(supportedMimeTypes);
  }

  @Override
  public WorkbookReader readWorkbook(SpreadsheetSource source) throws IOException {
    // Are we supported?
    Boolean supported = null;

    // What is our charset?
    String declaredEncoding = null;

    // If there is a MIME type and we support it, grab the charset.
    if (supported == null) {
      if (source.getMimeType().isPresent()) {
        MimeType mimeType = source.getMimeType().get();
        if (getSupportedMimeTypes().stream().anyMatch(t -> t.accepts(mimeType))) {
          supported = true;
          if (mimeType.getCharset().isPresent()) {
            declaredEncoding = mimeType.getCharset().get();
          }
        }
      }
    }

    // If there is a file extension, make sure it's one we support
    if (supported == null) {
      if (source.getFileExtension().isPresent()) {
        if (getSupportedFileExtensions().contains(source.getFileExtension().get())) {
          supported = true;
        }
      }
    }

    // If we're not supported, bail out.
    if (supported == null || !supported.booleanValue()) {
      throw new InvalidFileSpreadsheetException();
    }

    // If we have a hint that we're supported, then read without conformance check.
    CharSource decoded = null;
    try {
      decoded = MoreChardet.decode(source.getBytes(), declaredEncoding);
    } catch (IOException e) {
      // This is OK. It's just user input.
    }
    if (decoded != null) {
      return new CsvWorkbookReader(getConfig(), decoded, getFormat());
    }

    // Otherwise, try to read the workbook and see if it conforms to the format.
    return readWorkbook(source.getBytes());
  }

  @Override
  public CsvWorkbookReader readWorkbook(ByteSource source) throws IOException {
    CharSource decoded;
    try {
      decoded = MoreChardet.decode(source);
    } catch (IOException e) {
      throw new InvalidFileSpreadsheetException();
    }
    return readWorkbook(decoded);
  }

  public CsvWorkbookReader readWorkbook(CharSource source) throws IOException {
    if (!Csv.conforms(getFormat(), source))
      throw new InvalidFileSpreadsheetException();
    return new CsvWorkbookReader(getConfig(), source, getFormat());
  }

  @Override
  public WorksheetReader readActiveWorksheet(SpreadsheetSource source) throws IOException {
    // Are we supported?
    Boolean supported = null;

    // What is our charset?
    String declaredEncoding = null;

    // If there is a MIME type and we support it, grab the charset.
    if (supported == null) {
      if (source.getMimeType().isPresent()) {
        final MimeType mimeType = source.getMimeType().get();
        if (getSupportedMimeTypes().stream().anyMatch(t -> t.accepts(mimeType))) {
          supported = true;
          if (mimeType.getCharset().isPresent()) {
            declaredEncoding = mimeType.getCharset().get();
          }
        }
      }
    }

    // If there is a file extension, make sure it's one we support
    if (supported == null) {
      if (source.getFileExtension().isPresent()) {
        final String fileExtension = source.getFileExtension().get();
        if (getSupportedFileExtensions().contains(fileExtension)) {
          supported = true;
        }
      }
    }

    // If we're not supported, bail out.
    if (supported == null || !supported.booleanValue()) {
      throw new InvalidFileSpreadsheetException();
    }

    // If we have a hint that we're supported, then read without conformance check.
    CharSource decoded = null;
    try {
      decoded = MoreChardet.decode(source.getBytes(), declaredEncoding);
    } catch (IOException e) {
      // This is OK. It's just user input.
    }
    if (decoded != null) {
      return new CsvWorksheetReader(getConfig(), new CsvReader(getFormat(), decoded.getReader()));
    }

    // Otherwise, try to read the workbook and see if it conforms to the format.
    return readActiveWorksheet(source.getBytes());
  }

  @Override
  public CsvWorksheetReader readActiveWorksheet(ByteSource source) throws IOException {
    CharSource decoded;
    try {
      decoded = MoreChardet.decode(source);
    } catch (IOException e) {
      throw new InvalidFileSpreadsheetException();
    }
    return readActiveWorksheet(decoded);
  }

  public CsvWorksheetReader readActiveWorksheet(CharSource source) throws IOException {
    // This is ticklish. The best test for conformance is to try to read the first two records and
    // see if they both (a) conform to the format, and (b) have the same number of fields. If both
    // of those are true, we can be pretty sure that the file is in the right format. However, that
    // approach is not foolproof, since it avoids the possibility of a file with only one record,
    // or headers and no records.
    if (!Csv.conforms(getFormat(), source))
      throw new InvalidFileSpreadsheetException();
    return new CsvWorksheetReader(getConfig(), new CsvReader(getFormat(), source.getReader()));
  }

  @Override
  public WorkbookWriter writeWorkbook(SpreadsheetSink sink) throws IOException {
    // Are we supported?
    Boolean supported = null;

    // What is our charset?
    String declaredEncoding = null;

    // If there is a MIME type and we support it, grab the charset.
    if (supported == null) {
      if (sink.getMimeType().isPresent()) {
        MimeType mimeType = sink.getMimeType().get();
        if (getSupportedMimeTypes().stream().anyMatch(t -> t.accepts(mimeType))) {
          supported = true;
          if (mimeType.getCharset().isPresent()) {
            declaredEncoding = mimeType.getCharset().get();
          }
        }
      }
    }

    // If there is a file extension, make sure it's one we support
    if (supported == null) {
      if (getSupportedFileExtensions().contains(sink.getFileExtension())) {
        supported = true;
      }
    }

    // If we're not supported, bail out.
    if (supported == null || !supported.booleanValue()) {
      throw new UnrecognizedFormatSpreadsheetException(sink.getFileExtension());
    }

    // If we have a hint that we're supported, then read without conformance check.
    Charset charset = null;
    if (declaredEncoding != null) {
      try {
        charset = Charset.forName(declaredEncoding);
      } catch (UnsupportedCharsetException e) {
        // This is fine. It's user input.
      }
    }
    if (charset != null) {
      return new CsvWorkbookWriter(getConfig(), sink.getBytes().asCharSink(charset), getFormat());
    }

    // Otherwise, try to read the workbook and see if it conforms to the format.
    return writeWorkbook(sink.getBytes());
  }

  @Override
  public CsvWorkbookWriter writeWorkbook(ByteSink sink) throws IOException {
    return writeWorkbook(sink.asCharSink(StandardCharsets.UTF_8));
  }

  public CsvWorkbookWriter writeWorkbook(CharSink sink) throws IOException {
    return new CsvWorkbookWriter(getConfig(), sink, getFormat());
  }

  @Override
  public WorksheetWriter writeActiveWorksheet(SpreadsheetSink sink) throws IOException {
    // Are we supported?
    Boolean supported = null;

    // What is our charset?
    String declaredEncoding = null;

    // If there is a MIME type and we support it, grab the charset.
    if (supported == null) {
      if (sink.getMimeType().isPresent()) {
        MimeType mimeType = sink.getMimeType().get();
        if (getSupportedMimeTypes().stream().anyMatch(t -> t.accepts(mimeType))) {
          supported = true;
          if (mimeType.getCharset().isPresent()) {
            declaredEncoding = mimeType.getCharset().get();
          }
        }
      }
    }

    // If there is a file extension, make sure it's one we support
    if (supported == null) {
      if (getSupportedFileExtensions().contains(sink.getFileExtension())) {
        supported = true;
      }
    }

    // If we're not supported, bail out.
    if (supported == null || !supported.booleanValue()) {
      throw new UnrecognizedFormatSpreadsheetException(sink.getFileExtension());
    }

    // If we have a hint that we're supported, then read without conformance check.
    Charset charset = null;
    if (declaredEncoding != null) {
      try {
        charset = Charset.forName(declaredEncoding);
      } catch (UnsupportedCharsetException e) {
        // This is fine. It's user input.
      }
    }
    if (charset != null) {
      return new CsvWorksheetWriter(getConfig(),
          new CsvWriter(getFormat(), sink.getBytes().asCharSink(charset).getWriter()));
    }

    // Otherwise, try to read the workbook and see if it conforms to the format.
    return writeActiveWorksheet(sink.getBytes());
  }

  @Override
  public CsvWorksheetWriter writeActiveWorksheet(ByteSink sink) throws IOException {
    return writeActiveWorksheet(sink.asCharSink(StandardCharsets.UTF_8));
  }

  public CsvWorksheetWriter writeActiveWorksheet(CharSink sink) throws IOException {
    return new CsvWorksheetWriter(getConfig(), new CsvWriter(getFormat(), sink.getWriter()));
  }

  /**
   * @return the config
   */
  public CsvConfigRegistry getConfig() {
    return config;
  }

  @Override
  public String getDefaultFileExtension() {
    return defaultFileExtension;
  }

  private Set<String> getSupportedFileExtensions() {
    return supportedFileExtensions;
  }

  @Override
  public MimeType getDefaultMimeType() {
    return defaultMimeType;
  }

  private Set<MimeType> getSupportedMimeTypes() {
    return supportedMimeTypes;
  }

  private CsvFormat getFormat() {
    return format;
  }
}
