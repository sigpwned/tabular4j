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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class JavaTimeCsvValueMapperFactory extends CsvValueMapperFactoryBase {
  public static final JavaTimeCsvValueMapperFactory INSTANCE = new JavaTimeCsvValueMapperFactory();

  private JavaTimeCsvValueMapperFactory() {
    addMapper(Instant.class, Instant::parse, Instant::toString);
    addMapper(LocalDate.class, LocalDate::parse, LocalDate::toString);
    addMapper(LocalTime.class, LocalTime::parse, LocalTime::toString);
    addMapper(LocalDateTime.class, LocalDateTime::parse, LocalDateTime::toString);
    addMapper(OffsetDateTime.class, s -> OffsetDateTime.ofInstant(Instant.parse(s), ZoneOffset.UTC),
        OffsetDateTime::toString);
    addMapper(ZonedDateTime.class, s -> ZonedDateTime.ofInstant(Instant.parse(s), ZoneOffset.UTC),
        ZonedDateTime::toString);
    addMapper(ZoneId.class, ZoneId::of, ZoneId::toString);
  }
}
