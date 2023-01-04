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
package com.sigpwned.tabular4j.model;

import java.lang.reflect.Type;
import java.util.Objects;
import com.sigpwned.tabular4j.type.GenericType;
import com.sigpwned.tabular4j.type.QualifiedType;

public class WorksheetCellValue {
  public static WorksheetCellValue of(Object value) {
    return new WorksheetCellValue(value);
  }

  public static WorksheetCellValue of(Class<?> klass, Object value) {
    return new WorksheetCellValue(klass, value);
  }

  public static WorksheetCellValue of(Type type, Object value) {
    return new WorksheetCellValue(type, value);
  }

  public static WorksheetCellValue of(GenericType<?> genericType, Object value) {
    return new WorksheetCellValue(genericType, value);
  }

  public static WorksheetCellValue of(QualifiedType<?> type, Object value) {
    return new WorksheetCellValue(type, value);
  }

  private final QualifiedType<?> type;
  private final Object value;

  public WorksheetCellValue(Object value) {
    this(value != null ? value.getClass() : null, value);
  }

  public WorksheetCellValue(Class<?> klass, Object value) {
    this(QualifiedType.of(klass), value);
  }

  public WorksheetCellValue(Type type, Object value) {
    this(QualifiedType.of(type), value);
  }

  public WorksheetCellValue(GenericType<?> genericType, Object value) {
    this(QualifiedType.of(genericType), value);
  }

  public WorksheetCellValue(QualifiedType<?> type, Object value) {
    this.type = type;
    this.value = value;
  }

  /**
   * @return the type
   */
  public QualifiedType<?> getType() {
    return type;
  }

  /**
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    WorksheetCellValue other = (WorksheetCellValue) obj;
    return Objects.equals(type, other.type) && Objects.equals(value, other.value);
  }

  @Override
  public String toString() {
    return "WorksheetCellValue [type=" + type + ", value=" + value + "]";
  }
}
