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
package com.sigpwned.spreadsheet4j.excel.mapper;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.apache.poi.ss.usermodel.Cell;
import com.sigpwned.spreadsheet4j.excel.ExcelConfigRegistry;
import com.sigpwned.spreadsheet4j.excel.ExcelValueMapper;
import com.sigpwned.spreadsheet4j.excel.ExcelValueMapperFactory;
import com.sigpwned.spreadsheet4j.type.QualifiedType;

/* default */ class ExcelValueMapperFactoryBase implements ExcelValueMapperFactory {
  private final Map<Class<?>, ExcelValueMapper> mappers;

  protected ExcelValueMapperFactoryBase() {
    this.mappers = new IdentityHashMap<>();
  }

  protected <T> void addValueMapper(Class<T> klass, BiConsumer<Cell, T> setValue,
      Function<Cell, T> getValue) {
    mappers.put(klass, new ExcelValueMapper() {
      @Override
      public void setValue(Cell cell, Object value) {
        setValue.accept(cell, klass.cast(value));
      }

      @Override
      public Object getValue(Cell cell) {
        return getValue.apply(cell);
      }
    });
  }

  @Override
  public Optional<ExcelValueMapper> buildValueMapper(QualifiedType<?> type,
      ExcelConfigRegistry registry) {
    if (!type.getQualifiers().isEmpty())
      return Optional.empty();
    return Optional.ofNullable(mappers.get(type.getType()));
  }
}
