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
package com.sigpwned.tabular4j.model;

import static java.util.Objects.requireNonNull;
import java.lang.reflect.Type;
import com.sigpwned.tabular4j.type.GenericType;
import com.sigpwned.tabular4j.type.QualifiedType;

public class TabularWorksheetCell implements WorksheetCell {
  private final WorksheetCell delegate;
  private final String columnName;

  public TabularWorksheetCell(WorksheetCell delegate, String columnName) {
    this.delegate = requireNonNull(delegate);
    this.columnName = requireNonNull(columnName);
  }

  /**
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetCell#getColumnIndex()
   */
  public int getColumnIndex() {
    return delegate.getColumnIndex();
  }

  /**
   * @param type
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetCell#getValue(java.lang.reflect.Type)
   */
  public Object getValue(Type type) {
    return delegate.getValue(type);
  }

  /**
   * @param <T>
   * @param klass
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetCell#getValue(java.lang.Class)
   */
  public <T> T getValue(Class<T> klass) {
    return delegate.getValue(klass);
  }

  /**
   * @param <T>
   * @param genericType
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetCell#getValue(com.sigpwned.tabular4j.type.GenericType)
   */
  public <T> T getValue(GenericType<T> genericType) {
    return delegate.getValue(genericType);
  }

  /**
   * @param <T>
   * @param qualifiedType
   * @return
   * @see com.sigpwned.tabular4j.model.WorksheetCell#getValue(com.sigpwned.tabular4j.type.QualifiedType)
   */
  public <T> T getValue(QualifiedType<T> qualifiedType) {
    return delegate.getValue(qualifiedType);
  }

  public String getColumnName() {
    return columnName;
  }
}
