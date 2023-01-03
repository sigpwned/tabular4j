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
package com.sigpwned.spreadsheet4j.csv.util;

import static java.util.Collections.unmodifiableMap;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.sigpwned.csv4j.CsvField;
import com.sigpwned.spreadsheet4j.csv.CsvConfigRegistry;
import com.sigpwned.spreadsheet4j.csv.CsvValueMapper;
import com.sigpwned.spreadsheet4j.csv.CsvValueMapperFactory;
import com.sigpwned.spreadsheet4j.type.QualifiedType;

public class CoreCsvValueMapperFactory implements CsvValueMapperFactory {
  public static final CoreCsvValueMapperFactory INSTANCE = new CoreCsvValueMapperFactory();

  private static final Map<Type, CsvValueMapper> VALUE_MAPPERS;
  static {
    Map<Type, CsvValueMapper> valueMappers = new HashMap<>();

    // Boolean Primitives
    valueMappers.put(Boolean.class, new CsvValueMapper() {
      @Override
      public void setValue(CsvField cell, Object value) {
        Boolean x = (Boolean) value;
        cell.setQuoted(true);
        cell.setText(x.toString());
      }

      @Override
      public Object getValue(CsvField cell) {
        return Boolean.parseBoolean(cell.getText());
      }
    });

    // Integer primitives
    valueMappers.put(Byte.class, new CsvValueMapper() {
      @Override
      public void setValue(CsvField cell, Object value) {
        Byte x = (Byte) value;
        cell.setQuoted(true);
        cell.setText(x.toString());
      }

      @Override
      public Object getValue(CsvField cell) {
        return Byte.parseByte(cell.getText());
      }
    });
    valueMappers.put(Short.class, new CsvValueMapper() {
      @Override
      public void setValue(CsvField cell, Object value) {
        Short x = (Short) value;
        cell.setQuoted(true);
        cell.setText(x.toString());
      }

      @Override
      public Object getValue(CsvField cell) {
        return Short.parseShort(cell.getText());
      }
    });
    valueMappers.put(Integer.class, new CsvValueMapper() {
      @Override
      public void setValue(CsvField cell, Object value) {
        Integer x = (Integer) value;
        cell.setQuoted(true);
        cell.setText(x.toString());
      }

      @Override
      public Object getValue(CsvField cell) {
        return Integer.parseInt(cell.getText());
      }
    });
    valueMappers.put(Long.class, new CsvValueMapper() {
      @Override
      public void setValue(CsvField cell, Object value) {
        Long x = (Long) value;
        cell.setQuoted(true);
        cell.setText(x.toString());
      }

      @Override
      public Object getValue(CsvField cell) {
        return Long.parseLong(cell.getText());
      }
    });

    // Floating point primitives
    valueMappers.put(Float.class, new CsvValueMapper() {
      @Override
      public void setValue(CsvField cell, Object value) {
        Float x = (Float) value;
        cell.setQuoted(true);
        cell.setText(x.toString());
      }

      @Override
      public Object getValue(CsvField cell) {
        return Float.parseFloat(cell.getText());
      }
    });
    valueMappers.put(Double.class, new CsvValueMapper() {
      @Override
      public void setValue(CsvField cell, Object value) {
        Double x = (Double) value;
        cell.setQuoted(true);
        cell.setText(x.toString());
      }

      @Override
      public Object getValue(CsvField cell) {
        return Double.parseDouble(cell.getText());
      }
    });

    // Text primitives
    valueMappers.put(Character.class, new CsvValueMapper() {
      @Override
      public void setValue(CsvField cell, Object value) {
        Character x = (Character) value;
        cell.setQuoted(true);
        cell.setText(x.toString());
      }

      @Override
      public Object getValue(CsvField cell) {
        if (cell.getText().isEmpty())
          throw new IllegalStateException("empty");
        return cell.getText().charAt(0);
      }
    });
    valueMappers.put(String.class, new CsvValueMapper() {
      @Override
      public void setValue(CsvField cell, Object value) {
        String x = (String) value;
        cell.setQuoted(true);
        cell.setText(x);
      }

      @Override
      public Object getValue(CsvField cell) {
        return cell.getText();
      }
    });

    VALUE_MAPPERS = unmodifiableMap(valueMappers);
  }

  @Override
  public Optional<CsvValueMapper> buildValueMapper(QualifiedType<?> type,
      CsvConfigRegistry registry) {
    if (!type.getQualifiers().isEmpty())
      return Optional.empty();
    return Optional.ofNullable(VALUE_MAPPERS.get(type.getType()));
  }
}
