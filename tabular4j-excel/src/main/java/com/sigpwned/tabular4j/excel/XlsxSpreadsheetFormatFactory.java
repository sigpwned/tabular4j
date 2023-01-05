/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-xlsx
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
package com.sigpwned.tabular4j.excel;

import static java.util.Objects.requireNonNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.poi.UnsupportedFileFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.sigpwned.tabular4j.excel.read.ExcelWorkbookReader;
import com.sigpwned.tabular4j.excel.write.ExcelWorkbookWriter;
import com.sigpwned.tabular4j.excel.write.ExcelWorksheetWriter;
import com.sigpwned.tabular4j.exception.InvalidFileSpreadsheetException;
import com.sigpwned.tabular4j.forwarding.ForwardingWorkbookReader;
import com.sigpwned.tabular4j.forwarding.ForwardingWorkbookWriter;
import com.sigpwned.tabular4j.forwarding.ForwardingWorksheetReader;
import com.sigpwned.tabular4j.forwarding.ForwardingWorksheetWriter;
import com.sigpwned.tabular4j.io.ByteSink;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.io.sink.FileByteSink;
import com.sigpwned.tabular4j.io.source.FileByteSource;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorkbookWriter;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetWriter;
import com.sigpwned.tabular4j.util.MoreFiles;

public class XlsxSpreadsheetFormatFactory implements ExcelSpreadsheetFormatFactory {
  public static final String DEFAULT_FILE_EXTENSION = "xlsx";

  private final ExcelConfigRegistry config;

  public XlsxSpreadsheetFormatFactory() {
    this(new ExcelConfigRegistry());
  }

  public XlsxSpreadsheetFormatFactory(ExcelConfigRegistry config) {
    this.config = requireNonNull(config);
  }

  @Override
  public WorkbookReader readWorkbook(ByteSource source) throws IOException {
    if (source instanceof FileByteSource)
      return readWorkbook(((FileByteSource) source).getFile());

    WorkbookReader result = null;

    File file = MoreFiles.createTempFile("workbook.", ".xlsx");
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
  public WorkbookReader readWorkbook(File file) throws IOException {
    XSSFWorkbook workbook;
    try {
      workbook = new XSSFWorkbook(file);
    } catch (InvalidFormatException | UnsupportedFileFormatException e) {
      throw new InvalidFileSpreadsheetException();
    }
    return new ExcelWorkbookReader(getConfig(), workbook);
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
  public WorkbookWriter writeWorkbook(ByteSink sink) throws IOException {
    XSSFWorkbook workbook = new XSSFWorkbook();
    WorkbookWriter delegate = new ExcelWorkbookWriter(getConfig(), workbook);
    return new ForwardingWorkbookWriter(delegate) {
      @Override
      public void close() throws IOException {
        try {
          try (OutputStream out = sink.getOutputStream()) {
            workbook.write(out);
          }
        } finally {
          super.close();
        }
      }
    };
  }

  @Override
  public WorkbookWriter writeWorkbook(File file) throws IOException {
    return writeWorkbook(new FileByteSink(file));
  }

  @Override
  public WorksheetWriter writeActiveWorksheet(ByteSink sink) throws IOException {
    final XSSFWorkbook workbook = new XSSFWorkbook();
    final XSSFSheet worksheet = workbook.createSheet("main");
    final WorksheetWriter delegate = new ExcelWorksheetWriter(getConfig(), worksheet, 0);
    return new ForwardingWorksheetWriter(delegate) {
      @Override
      public void close() throws IOException {
        try {
          try (OutputStream out = sink.getOutputStream()) {
            workbook.write(out);
          }
        } finally {
          try {
            super.close();
          } finally {
            workbook.close();
          }
        }
      }
    };
  }

  @Override
  public WorksheetWriter writeActiveWorksheet(File file) throws IOException {
    return writeActiveWorksheet(new FileByteSink(file));
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
    return DEFAULT_FILE_EXTENSION;
  }
}
