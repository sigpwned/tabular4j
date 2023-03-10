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

public class BoxedCsvValueMapperFactory extends CsvValueMapperFactoryBase {
  public static final BoxedCsvValueMapperFactory INSTANCE = new BoxedCsvValueMapperFactory();

  private BoxedCsvValueMapperFactory() {
    addMapper(Boolean.class, Boolean::parseBoolean);
    addMapper(Byte.class, Byte::parseByte);
    addMapper(Short.class, Short::parseShort);
    addMapper(Integer.class, Integer::parseInt);
    addMapper(Long.class, Long::parseLong);
    addMapper(Float.class, Float::parseFloat);
    addMapper(Double.class, Double::parseDouble);
    addMapper(Character.class, s -> s.isEmpty() ? null : s.charAt(0));
  }
}
