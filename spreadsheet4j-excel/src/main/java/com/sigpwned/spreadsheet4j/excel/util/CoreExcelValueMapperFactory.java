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
package com.sigpwned.spreadsheet4j.excel.util;

import static java.util.Collections.unmodifiableMap;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import com.sigpwned.spreadsheet4j.excel.ExcelConfigRegistry;
import com.sigpwned.spreadsheet4j.excel.ExcelValueMapper;
import com.sigpwned.spreadsheet4j.excel.ExcelValueMapperFactory;
import com.sigpwned.spreadsheet4j.type.QualifiedType;

public class CoreExcelValueMapperFactory implements ExcelValueMapperFactory {
  public static final CoreExcelValueMapperFactory INSTANCE = new CoreExcelValueMapperFactory();

  private static final Map<Type, ExcelValueMapper> VALUE_MAPPERS;
  static {
    Map<Type, ExcelValueMapper> valueMappers = new HashMap<>();

    // Boolean Primitives
    valueMappers.put(Boolean.class, new ExcelValueMapper() {
      @Override
      public void setValue(Cell cell, Object value) {
        Boolean x = (Boolean) value;
        cell.setCellValue(x.booleanValue());
      }

      @Override
      public Object getValue(Cell cell) {
        Boolean x = cell.getBooleanCellValue();
        return x;
      }
    });

    // Integer primitives
    valueMappers.put(Byte.class, new ExcelValueMapper() {
      @Override
      public void setValue(Cell cell, Object value) {
        Byte x = (Byte) value;
        cell.setCellValue(x.doubleValue());
      }

      @Override
      public Object getValue(Cell cell) {
        Double x = cell.getNumericCellValue();
        return x.byteValue();
      }
    });
    valueMappers.put(Short.class, new ExcelValueMapper() {
      @Override
      public void setValue(Cell cell, Object value) {
        Short x = (Short) value;
        cell.setCellValue(x.doubleValue());
      }

      @Override
      public Object getValue(Cell cell) {
        Double x = cell.getNumericCellValue();
        return x.shortValue();
      }
    });
    valueMappers.put(Integer.class, new ExcelValueMapper() {
      @Override
      public void setValue(Cell cell, Object value) {
        Integer x = (Integer) value;
        cell.setCellValue(x.doubleValue());
      }

      @Override
      public Object getValue(Cell cell) {
        Double x = cell.getNumericCellValue();
        return x.intValue();
      }
    });
    valueMappers.put(Long.class, new ExcelValueMapper() {
      @Override
      public void setValue(Cell cell, Object value) {
        Long x = (Long) value;
        cell.setCellValue(x.doubleValue());
      }

      @Override
      public Object getValue(Cell cell) {
        Double x = cell.getNumericCellValue();
        return x.longValue();
      }
    });

    // Floating point primitives
    valueMappers.put(Float.class, new ExcelValueMapper() {
      @Override
      public void setValue(Cell cell, Object value) {
        Float x = (Float) value;
        cell.setCellValue(x.floatValue());
      }

      @Override
      public Object getValue(Cell cell) {
        Double x = cell.getNumericCellValue();
        return x.floatValue();
      }
    });
    valueMappers.put(Double.class, new ExcelValueMapper() {
      @Override
      public void setValue(Cell cell, Object value) {
        Double x = (Double) value;
        cell.setCellValue(x.doubleValue());
      }

      @Override
      public Object getValue(Cell cell) {
        Double x = cell.getNumericCellValue();
        return x.doubleValue();
      }
    });

    // Text primitives
    valueMappers.put(Character.class, new ExcelValueMapper() {
      @Override
      public void setValue(Cell cell, Object value) {
        Character x = (Character) value;
        cell.setCellValue(x.toString());
      }

      @Override
      public Object getValue(Cell cell) {
        String s = cell.getStringCellValue();
        if (s.isEmpty())
          throw new IllegalStateException("empty");
        return s.charAt(0);
      }
    });
    valueMappers.put(String.class, new ExcelValueMapper() {
      @Override
      public void setValue(Cell cell, Object value) {
        String x = (String) value;
        cell.setCellValue(x);
      }

      @Override
      public Object getValue(Cell cell) {
        String s = cell.getStringCellValue();
        return s;
      }
    });

    VALUE_MAPPERS = unmodifiableMap(valueMappers);
  }

  @Override
  public Optional<ExcelValueMapper> buildValueMapper(QualifiedType<?> type,
      ExcelConfigRegistry registry) {
    if (!type.getQualifiers().isEmpty())
      return Optional.empty();
    return Optional.ofNullable(VALUE_MAPPERS.get(type.getType()));
  }
}
