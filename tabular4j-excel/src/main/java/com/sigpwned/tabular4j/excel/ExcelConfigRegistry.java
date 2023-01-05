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

import static java.util.Collections.unmodifiableList;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import com.sigpwned.tabular4j.excel.mapper.BoxedExcelValueMapperFactory;
import com.sigpwned.tabular4j.excel.mapper.CoreExcelValueMapperFactory;
import com.sigpwned.tabular4j.excel.mapper.InternetExcelValueMapperFactory;
import com.sigpwned.tabular4j.excel.mapper.JavaTimeExcelValueMapperFactory;
import com.sigpwned.tabular4j.excel.mapper.PrimitiveExcelValueMapperFactory;
import com.sigpwned.tabular4j.type.GenericType;
import com.sigpwned.tabular4j.type.QualifiedType;

public class ExcelConfigRegistry {
  private final List<ExcelValueMapperFactory> valueMapperFactories;
  private final ExcelValueMapperFactory nullValueMapperFactory;

  public ExcelConfigRegistry() {
    this.valueMapperFactories = new ArrayList<>();
    addValueMapperLast(CoreExcelValueMapperFactory.INSTANCE);
    addValueMapperLast(PrimitiveExcelValueMapperFactory.INSTANCE);
    addValueMapperLast(BoxedExcelValueMapperFactory.INSTANCE);
    addValueMapperLast(JavaTimeExcelValueMapperFactory.INSTANCE);
    addValueMapperLast(InternetExcelValueMapperFactory.INSTANCE);
    this.nullValueMapperFactory = new ExcelValueMapperFactory() {
      @Override
      public Optional<ExcelValueMapper> buildValueMapper(QualifiedType<?> type,
          ExcelConfigRegistry registry) {
        return type != null ? Optional.empty() : Optional.of(new ExcelValueMapper() {
          @Override
          public void setValue(Cell cell, Object value) {
            if (value != null)
              throw new IllegalArgumentException("value must be null");
            cell.setBlank();
          }

          @Override
          public Object getValue(Cell cell) {
            return null;
          }
        });

      }
    };
  }

  public void addValueMapperFirst(ExcelValueMapperFactory valueMapperFactory) {
    valueMapperFactories.add(0, valueMapperFactory);
  }

  public void addValueMapperLast(ExcelValueMapperFactory valueMapperFactory) {
    valueMapperFactories.add(valueMapperFactories.size(), valueMapperFactory);
  }

  public Optional<ExcelValueMapper> findValueMapperForType(Class<?> klass) {
    return findValueMapperForType(QualifiedType.of(klass));
  }

  public Optional<ExcelValueMapper> findValueMapperForType(Type type) {
    return findValueMapperForType(QualifiedType.of(type));
  }

  public Optional<ExcelValueMapper> findValueMapperForType(GenericType<?> genericType) {
    return findValueMapperForType(QualifiedType.of(genericType));
  }

  public Optional<ExcelValueMapper> findValueMapperForType(QualifiedType<?> type) {
    if (type == null)
      return getNullValueMapperFactory().buildValueMapper((QualifiedType<?>) null, this);
    return getValueMapperFactories().stream()
        .flatMap(f -> f.buildValueMapper(type, ExcelConfigRegistry.this).stream()).findFirst();
  }

  /**
   * @return the nullValueMapperFactory
   */
  public ExcelValueMapperFactory getNullValueMapperFactory() {
    return nullValueMapperFactory;
  }

  /**
   * @return the mapperFactories
   */
  public List<ExcelValueMapperFactory> getValueMapperFactories() {
    return unmodifiableList(valueMapperFactories);
  }
}
