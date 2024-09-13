/*-
 * =================================LICENSE_START==================================
 * tabular4j-excel
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
package com.sigpwned.tabular4j.excel;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import com.sigpwned.tabular4j.exception.InvalidFileSpreadsheetException;
import com.sigpwned.tabular4j.exception.UnrecognizedFormatSpreadsheetException;
import com.sigpwned.tabular4j.forwarding.ForwardingWorkbookReader;
import com.sigpwned.tabular4j.forwarding.ForwardingWorksheetReader;
import com.sigpwned.tabular4j.forwarding.ForwardingWorksheetWriter;
import com.sigpwned.tabular4j.io.ByteSink;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.io.SpreadsheetSink;
import com.sigpwned.tabular4j.io.SpreadsheetSource;
import com.sigpwned.tabular4j.io.sink.FileByteSink;
import com.sigpwned.tabular4j.io.source.FileByteSource;
import com.sigpwned.tabular4j.mime.MimeType;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorkbookWriter;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetWriter;
import com.sigpwned.tabular4j.util.MoreFiles;

public abstract class ExcelSpreadsheetFormatFactoryBase implements ExcelSpreadsheetFormatFactory {
  private final ExcelConfigRegistry config;
  private final String defaultFileExtension;
  private final Set<String> supportedFileExtensions;
  private final MimeType defaultMimeType;
  private final Set<MimeType> supportedMimeTypes;

  protected ExcelSpreadsheetFormatFactoryBase(ExcelConfigRegistry config,
      String defaultFileExtension, Set<String> supportedFileExtensions, MimeType defaultMimeType,
      Set<MimeType> supportedMimeTypes) {
    this.config = requireNonNull(config);
    this.defaultFileExtension = requireNonNull(defaultFileExtension);
    this.supportedFileExtensions = unmodifiableSet(supportedFileExtensions);
    this.defaultMimeType = requireNonNull(defaultMimeType);
    this.supportedMimeTypes = unmodifiableSet(supportedMimeTypes);
  }

  @Override
  public WorkbookReader readWorkbook(SpreadsheetSource source) throws IOException {
    boolean supported = false;

    // If there is a file extension, make sure it's one we support
    if (source.getFileExtension().isPresent()
        && getSupportedFileExtensions().contains(source.getFileExtension().get())) {
      supported = true;
    }

    // If there is a MIME type, make sure it's one we support
    if (source.getMimeType().isPresent()
        && !getSupportedMimeTypes().stream().anyMatch(t -> t.accepts(source.getMimeType().get()))) {
      supported = true;
    }

    if (!supported)
      throw new InvalidFileSpreadsheetException();

    return readWorkbook(source.getBytes());
  }

  @Override
  public WorkbookReader readWorkbook(ByteSource source) throws IOException {
    if (source instanceof FileByteSource)
      return readWorkbook(((FileByteSource) source).getFile());

    WorkbookReader result = null;

    File file = MoreFiles.createTempFile("workbook.", "." + getDefaultFileExtension());
    try {
      try (OutputStream out = new FileOutputStream(file)) {
        try (InputStream in = source.getInputStream()) {
          in.transferTo(out);
        }
      }

      WorkbookReader delegate = readWorkbook(file);

      result = new ForwardingWorkbookReader(delegate) {
        @Override
        public void close() throws IOException {
          try {
            super.close();
          } finally {
            file.delete();
          }
        }
      };
    } finally {
      if (result == null)
        file.delete();
    }

    return result;
  }

  @Override
  public WorksheetReader readActiveWorksheet(SpreadsheetSource source) throws IOException {
    boolean supported = false;

    // If there is a file extension, make sure it's one we support
    if (source.getFileExtension().isPresent()
        && getSupportedFileExtensions().contains(source.getFileExtension().get())) {
      supported = true;
    }

    // If there is a MIME type, make sure it's one we support
    if (source.getMimeType().isPresent()
        && getSupportedMimeTypes().stream().anyMatch(t -> t.accepts(source.getMimeType().get()))) {
      supported = true;
    }

    if (!supported)
      throw new InvalidFileSpreadsheetException();

    return readActiveWorksheet(source.getBytes());
  }

  @Override
  public WorksheetReader readActiveWorksheet(ByteSource source) throws IOException {
    final WorkbookReader workbook = readWorkbook(source);
    final WorksheetReader delegate = workbook.getActiveWorksheet();
    return new ForwardingWorksheetReader(delegate) {
      @Override
      public void close() throws IOException {
        try {
          super.close();
        } finally {
          workbook.close();
        }
      }
    };
  }

  @Override
  public WorksheetReader readActiveWorksheet(File file) throws IOException {
    final WorkbookReader workbook = readWorkbook(file);
    final WorksheetReader delegate = workbook.getActiveWorksheet();
    return new ForwardingWorksheetReader(delegate) {
      @Override
      public void close() throws IOException {
        try {
          super.close();
        } finally {
          workbook.close();
        }
      }
    };
  }

  @Override
  public WorkbookWriter writeWorkbook(SpreadsheetSink sink) throws IOException {
    boolean supported = false;

    if (sink.getFileExtension().equals(getDefaultFileExtension()))
      supported = true;

    if (sink.getMimeType().isPresent()) {
      MimeType mimeType = sink.getMimeType().get();
      if (getDefaultMimeType().accepts(mimeType)) {
        supported = true;
      }
    }

    if (!supported)
      throw new UnrecognizedFormatSpreadsheetException(sink.getFileExtension());

    return writeWorkbook(sink.getBytes());
  }

  @Override
  public WorkbookWriter writeWorkbook(File file) throws IOException {
    return writeWorkbook(new FileByteSink(file));
  }

  @Override
  public WorksheetWriter writeActiveWorksheet(SpreadsheetSink sink) throws IOException {
    boolean supported = false;

    if (sink.getFileExtension().equals(getDefaultFileExtension()))
      supported = true;

    if (sink.getMimeType().isPresent()) {
      MimeType mimeType = sink.getMimeType().get();
      if (getDefaultMimeType().accepts(mimeType)) {
        supported = true;
      }
    }

    if (!supported)
      throw new UnrecognizedFormatSpreadsheetException(sink.getFileExtension());

    return writeActiveWorksheet(sink.getBytes());
  }

  @Override
  public WorksheetWriter writeActiveWorksheet(ByteSink sink) throws IOException {
    final WorkbookWriter wb = writeWorkbook(sink);
    final WorksheetWriter ws = wb.getWorksheet("main");
    return new ForwardingWorksheetWriter(ws) {
      @Override
      public void close() throws IOException {
        try {
          super.close();
        } finally {
          wb.close();
        }
      }
    };
  }

  @Override
  public WorksheetWriter writeActiveWorksheet(File sink) throws IOException {
    return writeActiveWorksheet(new FileByteSink(sink));
  }

  /**
   * @return the config
   */
  @Override
  public ExcelConfigRegistry getConfig() {
    return config;
  }

  @Override
  public String getDefaultFileExtension() {
    return defaultFileExtension;
  }

  /**
   * The file extensions this format can read. Must include {@link #getDefaultFileExtension()}.
   */
  protected Set<String> getSupportedFileExtensions() {
    return supportedFileExtensions;
  }

  @Override
  public MimeType getDefaultMimeType() {
    return defaultMimeType;
  }

  /**
   * The MIME types this format can read.
   */
  protected Set<MimeType> getSupportedMimeTypes() {
    return supportedMimeTypes;
  }
}
