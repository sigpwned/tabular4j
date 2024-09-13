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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.sigpwned.tabular4j.excel.read.ExcelWorkbookReader;
import com.sigpwned.tabular4j.excel.util.Excel;
import com.sigpwned.tabular4j.excel.write.ExcelWorkbookWriter;
import com.sigpwned.tabular4j.exception.InvalidFileSpreadsheetException;
import com.sigpwned.tabular4j.forwarding.ForwardingWorkbookWriter;
import com.sigpwned.tabular4j.io.ByteSink;
import com.sigpwned.tabular4j.mime.MimeType;
import com.sigpwned.tabular4j.model.WorkbookReader;
import com.sigpwned.tabular4j.model.WorkbookWriter;

public class XlsxSpreadsheetFormatFactory extends ExcelSpreadsheetFormatFactoryBase {
  public static final String DEFAULT_FILE_EXTENSION = "xlsx";

  public static final Set<String> SUPPORTED_FILE_EXTENSIONS = Set.of(DEFAULT_FILE_EXTENSION);

  public static final MimeType DEFAULT_MIME_TYPE =
      MimeType.of("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");

  public static final Set<MimeType> SUPPORTED_MIME_TYPES = Set.of(DEFAULT_MIME_TYPE);

  public XlsxSpreadsheetFormatFactory() {
    this(new ExcelConfigRegistry());
  }

  public XlsxSpreadsheetFormatFactory(ExcelConfigRegistry config) {
    super(config, DEFAULT_FILE_EXTENSION, SUPPORTED_FILE_EXTENSIONS, DEFAULT_MIME_TYPE,
        SUPPORTED_MIME_TYPES);
  }

  @Override
  public WorkbookReader readWorkbook(File file) throws IOException {
    if (!Excel.isPossiblyXlsxFile(file))
      throw new InvalidFileSpreadsheetException();
    XSSFWorkbook workbook;
    try {
      workbook = new XSSFWorkbook(file);
    } catch (InvalidFormatException e) {
      throw new InvalidFileSpreadsheetException();
    }
    return new ExcelWorkbookReader(getConfig(), workbook);
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
}
