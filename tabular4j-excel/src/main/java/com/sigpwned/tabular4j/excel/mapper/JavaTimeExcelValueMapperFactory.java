/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-excel
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.apache.poi.ss.usermodel.Cell;

public class JavaTimeExcelValueMapperFactory extends ExcelValueMapperFactoryBase {
  public static final JavaTimeExcelValueMapperFactory INSTANCE =
      new JavaTimeExcelValueMapperFactory();

  private static final ZoneId Z = ZoneId.of("Z");

  private JavaTimeExcelValueMapperFactory() {
    addValueMapper(Instant.class,
        (c, x) -> c.setCellValue(x.atOffset(ZoneOffset.UTC).toLocalDateTime()),
        c -> c.getLocalDateTimeCellValue().atOffset(ZoneOffset.UTC).toInstant());
    addValueMapper(LocalDate.class, Cell::setCellValue,
        c -> c.getLocalDateTimeCellValue().toLocalDate());
    addValueMapper(LocalTime.class, (c, x) -> c.setCellValue(x.toString()),
        c -> LocalTime.parse(c.getStringCellValue()));
    addValueMapper(LocalDateTime.class, Cell::setCellValue, Cell::getLocalDateTimeCellValue);
    addValueMapper(OffsetDateTime.class,
        (c, x) -> c.setCellValue(x.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime()),
        c -> c.getLocalDateTimeCellValue().atOffset(ZoneOffset.UTC));
    addValueMapper(ZonedDateTime.class,
        (c, x) -> c.setCellValue(x.withZoneSameInstant(Z).toLocalDateTime()),
        c -> c.getLocalDateTimeCellValue().atZone(Z));
    addValueMapper(ZoneId.class, (c, x) -> c.setCellValue(x.toString()),
        c -> ZoneId.of(c.getStringCellValue()));
  }
}
