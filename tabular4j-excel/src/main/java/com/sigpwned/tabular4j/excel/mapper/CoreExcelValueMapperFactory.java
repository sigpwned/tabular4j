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
package com.sigpwned.tabular4j.excel.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Cell;

public class CoreExcelValueMapperFactory extends ExcelValueMapperFactoryBase {
  public static final CoreExcelValueMapperFactory INSTANCE = new CoreExcelValueMapperFactory();

  private CoreExcelValueMapperFactory() {
    addValueMapper(BigInteger.class, (c, x) -> c.setCellValue(x.toString()),
        c -> new BigInteger(c.getStringCellValue()));
    addValueMapper(BigDecimal.class, (c, x) -> c.setCellValue(x.toString()),
        c -> new BigDecimal(c.getStringCellValue()));
    addValueMapper(String.class, (c, x) -> c.setCellValue(x), Cell::getStringCellValue);
    addValueMapper(byte[].class, (c, x) -> c.setCellValue(Base64.getEncoder().encodeToString(x)),
        c -> Base64.getDecoder().decode(c.getStringCellValue()));
    addValueMapper(UUID.class, (c, x) -> c.setCellValue(x.toString()),
        c -> UUID.fromString(c.getStringCellValue()));
    addValueMapper(Date.class, (c, x) -> c.setCellValue(x), Cell::getDateCellValue);
    addValueMapper(Calendar.class, (c, x) -> c.setCellValue(x), c -> {
      Calendar result = Calendar.getInstance();
      result.setTime(c.getDateCellValue());
      return result;
    });
  }
}
