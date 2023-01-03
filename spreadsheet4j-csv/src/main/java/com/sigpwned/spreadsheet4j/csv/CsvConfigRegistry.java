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

import static java.util.Collections.unmodifiableList;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.sigpwned.spreadsheet4j.csv.util.CoreCsvValueMapperFactory;
import com.sigpwned.spreadsheet4j.type.GenericType;
import com.sigpwned.spreadsheet4j.type.QualifiedType;

public class CsvConfigRegistry {
  private final List<CsvValueMapperFactory> valueMapperFactories;

  public CsvConfigRegistry() {
    this.valueMapperFactories = new ArrayList<>();
    addValueMapperLast(CoreCsvValueMapperFactory.INSTANCE);
  }

  public void addValueMapperFirst(CsvValueMapperFactory valueMapperFactory) {
    valueMapperFactories.add(0, valueMapperFactory);
  }

  public void addValueMapperLast(CsvValueMapperFactory valueMapperFactory) {
    valueMapperFactories.add(valueMapperFactories.size(), valueMapperFactory);
  }

  public Optional<CsvValueMapper> findValueMapperForType(Class<?> klass) {
    return findValueMapperForType(QualifiedType.of(klass));
  }

  public Optional<CsvValueMapper> findValueMapperForType(Type type) {
    return findValueMapperForType(QualifiedType.of(type));
  }

  public Optional<CsvValueMapper> findValueMapperForType(GenericType<?> genericType) {
    return findValueMapperForType(QualifiedType.of(genericType));
  }

  public Optional<CsvValueMapper> findValueMapperForType(QualifiedType<?> type) {
    return getValueMapperFactories().stream()
        .flatMap(f -> f.buildValueMapper(type, CsvConfigRegistry.this).stream()).findFirst();
  }

  /**
   * @return the mapperFactories
   */
  public List<CsvValueMapperFactory> getValueMapperFactories() {
    return unmodifiableList(valueMapperFactories);
  }
}
