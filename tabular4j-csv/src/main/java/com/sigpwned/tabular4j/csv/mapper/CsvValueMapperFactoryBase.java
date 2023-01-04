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
package com.sigpwned.tabular4j.csv.mapper;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import com.sigpwned.csv4j.CsvField;
import com.sigpwned.tabular4j.csv.CsvConfigRegistry;
import com.sigpwned.tabular4j.csv.CsvValueMapper;
import com.sigpwned.tabular4j.csv.CsvValueMapperFactory;
import com.sigpwned.tabular4j.type.QualifiedType;

/* default */ abstract class CsvValueMapperFactoryBase implements CsvValueMapperFactory {
  private final Map<Class<?>, CsvValueMapper> mappers;

  protected CsvValueMapperFactoryBase() {
    this.mappers = new IdentityHashMap<>();
  }

  protected <T> void addMapper(Class<T> klass, Function<String, T> fromString) {
    addMapper(klass, fromString, Objects::toString);
  }

  protected <T> void addMapper(Class<T> klass, Function<String, T> fromString,
      Function<T, String> toString) {
    mappers.put(klass, new CsvValueMapper() {
      @Override
      public void setValue(CsvField cell, Object value) {
        T t = klass.cast(value);
        cell.setQuoted(true);
        cell.setText(toString.apply(t));
      }

      @Override
      public Object getValue(CsvField cell) {
        return klass.cast(fromString.apply(cell.getText()));
      }
    });
  }

  @Override
  public Optional<CsvValueMapper> buildValueMapper(QualifiedType<?> type,
      CsvConfigRegistry registry) {
    if (!type.getQualifiers().isEmpty())
      return Optional.empty();
    return Optional.ofNullable(mappers.get(type.getType()));
  }

}
