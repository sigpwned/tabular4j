/*-
 * =================================LICENSE_START==================================
 * tabular4j-core
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
package com.sigpwned.tabular4j.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Test;
import com.sigpwned.tabular4j.SpreadsheetFactory;
import com.sigpwned.tabular4j.model.TabularWorkbookWriter;
import com.sigpwned.tabular4j.model.TabularWorksheetRowWriter;
import com.sigpwned.tabular4j.model.WorksheetCellDefinition;

public class TabularWriteTest {
  @Test
  public void workbookTest() throws IOException {
    ByteArrayOutputStream drain = new ByteArrayOutputStream();

    try (TabularWorkbookWriter wb =
        new SpreadsheetFactory().writeTabularWorkbook(() -> drain, "test")) {
      try (TabularWorksheetRowWriter ws = wb.getWorksheet("test").writeHeaders("alpha", "bravo")) {
        ws.writeRow(List.of(WorksheetCellDefinition.ofValue("hello"),
            WorksheetCellDefinition.ofValue("world")));
        ws.writeRow(List.of(WorksheetCellDefinition.ofValue("a"),
            WorksheetCellDefinition.ofValue("b"), WorksheetCellDefinition.ofValue("c")));
        ws.writeRow(List.of(WorksheetCellDefinition.ofValue("1")));
      }
    }

    assertThat(new String(drain.toByteArray(), StandardCharsets.UTF_8),
        is("alpha,bravo\n" + "hello,world\n" + "a,b\n" + "1,\n"));
  }

  @Test
  public void worksheetTest() throws IOException {
    ByteArrayOutputStream drain = new ByteArrayOutputStream();

    try (TabularWorksheetRowWriter ws = new SpreadsheetFactory()
        .writeTabularActiveWorksheet(() -> drain, "test").writeHeaders("alpha", "bravo")) {
      ws.writeRow(List.of(WorksheetCellDefinition.ofValue("hello"),
          WorksheetCellDefinition.ofValue("world")));
      ws.writeRow(List.of(WorksheetCellDefinition.ofValue("a"),
          WorksheetCellDefinition.ofValue("b"), WorksheetCellDefinition.ofValue("c")));
      ws.writeRow(List.of(WorksheetCellDefinition.ofValue("1")));
    }

    assertThat(new String(drain.toByteArray(), StandardCharsets.UTF_8),
        is("alpha,bravo\n" + "hello,world\n" + "a,b\n" + "1,\n"));
  }
}
