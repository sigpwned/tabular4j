package com.sigpwned.spreadsheet4j.xlsx;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.sigpwned.spreadsheet4j.model.WorksheetCellDefinition;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorksheetRow;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class XlsxTest {
  @Test
  public void smokeTest() throws IOException {
    XlsxSpreadsheetFactory factory = new XlsxSpreadsheetFactory();

    List<Object> values = new ArrayList<>();
    File tmp = File.createTempFile("workbook.", ".xlsx.");
    try {
      System.out.println(tmp.getAbsolutePath());

      try (WorksheetWriter w = factory.writeActiveWorksheet(tmp)) {
        w.writeRow(List.of(WorksheetCellDefinition.ofValue("hello"),
            WorksheetCellDefinition.ofValue("world")));
      }

      try (WorksheetReader r = factory.readActiveWorksheet(tmp)) {
        WorksheetRow row = r.readRow();
        values.add(row.getCells().get(0).getValue(String.class));
        values.add(row.getCells().get(1).getValue(String.class));
      }
    } finally {
      tmp.delete();
    }

    assertThat(values, is(List.of("hello", "world")));
  }
}
