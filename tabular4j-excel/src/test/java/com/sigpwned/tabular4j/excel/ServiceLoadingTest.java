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
package com.sigpwned.tabular4j.excel;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;
import org.junit.Test;
import com.sigpwned.tabular4j.SpreadsheetFactory;
import com.sigpwned.tabular4j.SpreadsheetFormatFactory;

public class ServiceLoadingTest {
  @Test
  public void smokeTest() {
    assertThat(
        new SpreadsheetFactory().getFormats().stream()
            .map(SpreadsheetFormatFactory::getDefaultFileExtension).sorted().collect(toList()),
        is(List.of(XlsSpreadsheetFormatFactory.DEFAULT_FILE_EXTENSION,
            XlsxSpreadsheetFormatFactory.DEFAULT_FILE_EXTENSION)));
  }
}
