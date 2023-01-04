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

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;

public class InternetExcelValueMapperFactory extends ExcelValueMapperFactoryBase {
  public static final InternetExcelValueMapperFactory INSTANCE =
      new InternetExcelValueMapperFactory();

  private InternetExcelValueMapperFactory() {
    // TODO We should have a Hyperlink here
    addValueMapper(URL.class, (c, x) -> c.setCellValue(x.toString()), c -> {
      try {
        return new URL(c.getStringCellValue());
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException("Invalid URL: " + c.getStringCellValue());
      }
    });
    // TODO We should have a Hyperlink here
    addValueMapper(URI.class, (c, x) -> c.setCellValue(x.toString()),
        c -> URI.create(c.getStringCellValue()));
    addValueMapper(InetAddress.class, (c, x) -> c.setCellValue(x.getHostAddress()), c -> {
      try {
        return InetAddress.getByName(c.getStringCellValue());
      } catch (UnknownHostException e) {
        throw new IllegalArgumentException("Invalid hostname: " + c.getStringCellValue());
      }
    });
  }
}
