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
package com.sigpwned.spreadsheet4j.csv;

import com.sigpwned.csv4j.CsvField;
import com.sigpwned.spreadsheet4j.model.WorksheetCell;
import com.sigpwned.spreadsheet4j.type.QualifiedType;

public class CsvWorksheetCell implements WorksheetCell {
  private final int columnIndex;
  private final CsvField field;
  private final CsvConfigRegistry config;

  public CsvWorksheetCell(int columnIndex, CsvField field, CsvConfigRegistry config) {
    this.columnIndex = columnIndex;
    this.field = field;
    this.config = config;
  }

  @Override
  public int getColumnIndex() {
    return columnIndex;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getValue(QualifiedType<T> qualifiedType) {
    if (getField() == null)
      return null;
    if (!getField().isQuoted() && getField().getText().isEmpty())
      return null;
    return (T) getConfig().findValueMapperForType(qualifiedType).map(v -> v.getValue(getField()))
        .orElseThrow(() -> new IllegalStateException("no mapper for type"));
  }

  /**
   * @return the field
   */
  private CsvField getField() {
    return field;
  }

  /**
   * @return the config
   */
  private CsvConfigRegistry getConfig() {
    return config;
  }
}
