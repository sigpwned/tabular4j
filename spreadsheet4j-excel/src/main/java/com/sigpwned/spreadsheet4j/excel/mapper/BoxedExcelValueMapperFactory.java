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

public class BoxedExcelValueMapperFactory extends ExcelValueMapperFactoryBase {
  public static final BoxedExcelValueMapperFactory INSTANCE = new BoxedExcelValueMapperFactory();

  private BoxedExcelValueMapperFactory() {
    addValueMapper(Boolean.class, Cell::setCellValue, c -> (boolean) c.getBooleanCellValue());
    addValueMapper(Byte.class, Cell::setCellValue, c -> (byte) c.getNumericCellValue());
    addValueMapper(Short.class, Cell::setCellValue, c -> (short) c.getNumericCellValue());
    addValueMapper(Integer.class, Cell::setCellValue, c -> (int) c.getNumericCellValue());
    addValueMapper(Long.class, Cell::setCellValue, c -> (long) c.getNumericCellValue());
    addValueMapper(Float.class, Cell::setCellValue, c -> (float) c.getNumericCellValue());
    addValueMapper(Double.class, Cell::setCellValue, c -> (double) c.getNumericCellValue());
    addValueMapper(Character.class, (c, x) -> c.setCellValue(x.toString()),
        c -> c.getStringCellValue().isEmpty() ? null : c.getStringCellValue().charAt(0));
  }
}
