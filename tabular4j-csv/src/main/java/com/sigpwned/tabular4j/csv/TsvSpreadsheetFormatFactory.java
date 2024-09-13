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
package com.sigpwned.tabular4j.csv;

import java.util.Set;
import com.sigpwned.tabular4j.SpreadsheetFormatFactory;
import com.sigpwned.tabular4j.csv.util.Csv;
import com.sigpwned.tabular4j.mime.MimeType;

public class TsvSpreadsheetFormatFactory extends CsvFormatSpreadsheetFormatFactoryBase
    implements SpreadsheetFormatFactory {
  public static final String DEFAULT_FILE_EXTENSION = "tsv";

  public static final Set<String> SUPPORTED_FILE_EXTENSIONS = Set.of(DEFAULT_FILE_EXTENSION, "tab");

  public static final MimeType DEFAULT_MIME_TYPE = MimeType.of("text", "tsv");

  public static final Set<MimeType> SUPPORTED_MIME_TYPES =
      Set.of(DEFAULT_MIME_TYPE, MimeType.of("text", "tab-separated-values"));

  public TsvSpreadsheetFormatFactory() {
    this(new CsvConfigRegistry());
  }

  public TsvSpreadsheetFormatFactory(CsvConfigRegistry config) {
    super(config, Csv.TSV_FILE_FORMAT, DEFAULT_FILE_EXTENSION, SUPPORTED_FILE_EXTENSIONS,
        DEFAULT_MIME_TYPE, SUPPORTED_MIME_TYPES);
  }
}
