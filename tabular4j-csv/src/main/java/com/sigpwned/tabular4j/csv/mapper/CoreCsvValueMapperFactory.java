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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CoreCsvValueMapperFactory extends CsvValueMapperFactoryBase {
  public static final CoreCsvValueMapperFactory INSTANCE = new CoreCsvValueMapperFactory();

  private CoreCsvValueMapperFactory() {
    addMapper(BigInteger.class, BigInteger::new, BigInteger::toString);
    addMapper(BigDecimal.class, BigDecimal::new, BigDecimal::toString);
    addMapper(String.class, s -> s, x -> x);
    addMapper(byte[].class, s -> Base64.getDecoder().decode(s),
        x -> Base64.getEncoder().encodeToString(x));
    addMapper(UUID.class, UUID::fromString, UUID::toString);
    addMapper(Date.class, s -> Date.from(Instant.parse(s)), x -> x.toInstant().toString());
    addMapper(Calendar.class, s -> {
      Calendar c = Calendar.getInstance();
      c.setTime(Date.from(Instant.parse(s)));
      return c;
    }, x -> x.getTime().toInstant().atOffset(ZoneOffset.UTC).toString());
  }
}
