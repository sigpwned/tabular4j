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

import org.apache.poi.ss.usermodel.Cell;

public class PrimitiveExcelValueMapperFactory extends ExcelValueMapperFactoryBase {
  public static final PrimitiveExcelValueMapperFactory INSTANCE =
      new PrimitiveExcelValueMapperFactory();

  private PrimitiveExcelValueMapperFactory() {
    addValueMapper(boolean.class, Cell::setCellValue, c -> (boolean) c.getBooleanCellValue());
    addValueMapper(byte.class, Cell::setCellValue, c -> (byte) c.getNumericCellValue());
    addValueMapper(short.class, Cell::setCellValue, c -> (short) c.getNumericCellValue());
    addValueMapper(int.class, Cell::setCellValue, c -> (int) c.getNumericCellValue());
    addValueMapper(long.class, Cell::setCellValue, c -> (long) c.getNumericCellValue());
    addValueMapper(float.class, Cell::setCellValue, c -> (float) c.getNumericCellValue());
    addValueMapper(double.class, Cell::setCellValue, c -> (double) c.getNumericCellValue());
    addValueMapper(char.class, (c, x) -> c.setCellValue(x.toString()), c -> {
      if (c.getStringCellValue().isEmpty())
        throw new IllegalArgumentException("empty");
      return c.getStringCellValue().charAt(0);
    });
  }
}
