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
package com.sigpwned.spreadsheet4j.csv.mapper;

public class PrimitiveCsvValueMapperFactory extends CsvValueMapperFactoryBase {
  public static final PrimitiveCsvValueMapperFactory INSTANCE =
      new PrimitiveCsvValueMapperFactory();

  private PrimitiveCsvValueMapperFactory() {
    addMapper(boolean.class, Boolean::parseBoolean);
    addMapper(byte.class, Byte::parseByte);
    addMapper(short.class, Short::parseShort);
    addMapper(int.class, Integer::parseInt);
    addMapper(long.class, Long::parseLong);
    addMapper(float.class, Float::parseFloat);
    addMapper(double.class, Double::parseDouble);
    addMapper(char.class, s -> {
      if (s.isEmpty())
        throw new IllegalArgumentException("empty");
      return s.charAt(0);
    });
  }
}
