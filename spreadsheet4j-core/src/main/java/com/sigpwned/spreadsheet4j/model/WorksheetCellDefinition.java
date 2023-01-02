/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
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
package com.sigpwned.spreadsheet4j.model;

import static java.util.Objects.requireNonNull;

public class WorksheetCellDefinition {
  public static WorksheetCellDefinition ofValue(Object value) {
    return of(WorksheetCellValue.of(value));
  }

  public static WorksheetCellDefinition of(WorksheetCellValue value) {
    return new WorksheetCellDefinition(value);
  }

  public static WorksheetCellDefinition of(WorksheetCellStyle style, WorksheetCellValue value) {
    return new WorksheetCellDefinition(style, value);
  }

  private final WorksheetCellStyle style;
  private final WorksheetCellValue value;

  public WorksheetCellDefinition(WorksheetCellValue value) {
    this(null, value);
  }

  public WorksheetCellDefinition(WorksheetCellStyle style, WorksheetCellValue value) {
    this.style = style;
    this.value = requireNonNull(value);
  }

  /**
   * @return the style
   */
  public WorksheetCellStyle getStyle() {
    return style;
  }

  /**
   * @return the value
   */
  public WorksheetCellValue getValue() {
    return value;
  }
}
