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
package com.sigpwned.spreadsheet4j.excel;

import static java.util.Objects.requireNonNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.sigpwned.spreadsheet4j.excel.read.ExcelWorkbookReader;
import com.sigpwned.spreadsheet4j.excel.write.ExcelWorkbookWriter;
import com.sigpwned.spreadsheet4j.forwarding.ForwardingWorkbookReader;
import com.sigpwned.spreadsheet4j.forwarding.ForwardingWorkbookWriter;
import com.sigpwned.spreadsheet4j.forwarding.ForwardingWorksheetReader;
import com.sigpwned.spreadsheet4j.forwarding.ForwardingWorksheetWriter;
import com.sigpwned.spreadsheet4j.io.ByteSink;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.model.WorkbookReader;
import com.sigpwned.spreadsheet4j.model.WorkbookWriter;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class XlsSpreadsheetFormatFactory implements ExcelSpreadsheetFormatFactory {
  public static final String DEFAULT_FILE_EXTENSION = "xls";

  private final ExcelConfigRegistry config;

  public XlsSpreadsheetFormatFactory() {
    this(new ExcelConfigRegistry());
  }

  public XlsSpreadsheetFormatFactory(ExcelConfigRegistry config) {
    this.config = requireNonNull(config);
  }

  @Override
  public WorkbookReader readWorkbook(ByteSource source) throws IOException {
    WorkbookReader result = null;

    File file = File.createTempFile("workbook.", ".xls");
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
    HSSFWorkbook workbook;
    try {
      workbook = new HSSFWorkbook(POIFSFileSystem.create(file));
    } catch (IllegalArgumentException e) {
      throw new IOException("Failed to open workbook", e);
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
    HSSFWorkbook workbook = new HSSFWorkbook();
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
    return writeWorkbook(() -> new FileOutputStream(file));
  }

  @Override
  public WorksheetWriter writeActiveWorksheet(ByteSink sink) throws IOException {
    final HSSFWorkbook workbook = new HSSFWorkbook();
    final WorkbookWriter parent = new ExcelWorkbookWriter(getConfig(), workbook);
    final WorksheetWriter delegate = parent.getWorksheet("main");
    return new ForwardingWorksheetWriter(delegate) {
      @Override
      public void close() throws IOException {
        try {
          try (OutputStream out = sink.getOutputStream()) {
            workbook.write(out);
          }
        } finally {
          super.close();
          parent.close();
        }
      }
    };
  }

  @Override
  public WorksheetWriter writeActiveWorksheet(File file) throws IOException {
    return writeActiveWorksheet(() -> new FileOutputStream(file));
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
