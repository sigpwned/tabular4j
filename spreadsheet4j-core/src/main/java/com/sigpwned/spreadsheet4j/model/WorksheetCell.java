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

import java.lang.reflect.Type;
import com.sigpwned.spreadsheet4j.type.GenericType;
import com.sigpwned.spreadsheet4j.type.QualifiedType;

public interface WorksheetCell {
  public int getColumnIndex();

  public default Object getValue(Type type) {
    return getValue(QualifiedType.of(type));
  }

  @SuppressWarnings("unchecked")
  public default <T> T getValue(Class<T> klass) {
    return (T) getValue((Type) klass);
  }

  @SuppressWarnings("unchecked")
  public default <T> T getValue(GenericType<T> genericType) {
    return (T) getValue(genericType.getType());
  }

  public <T> T getValue(QualifiedType<T> qualifiedType);
}
