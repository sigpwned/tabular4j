package com.sigpwned.spreadsheet4j.xlsx;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.sigpwned.spreadsheet4j.io.ByteSource;
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

  @Test(expected = IOException.class)
  public void mergedRegionTest() throws Exception {
    XlsxSpreadsheetFactory factory = new XlsxSpreadsheetFactory();

    ByteSource source = () -> Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("has-merged-region.xlsx");

    try (WorksheetReader r = factory.readActiveWorksheet(source)) {
      // This should throw an exception
      r.readRow();
    }
  }

  @Test
  public void hiddenRowsAndColsTest() throws Exception {
    XlsxSpreadsheetFactory factory = new XlsxSpreadsheetFactory();

    ByteSource source = () -> Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("has-hidden-rows-and-cols.xlsx");

    List<List<String>> content = new ArrayList<>();
    try (WorksheetReader r = factory.readActiveWorksheet(source)) {
      // This should throw an exception
      for (WorksheetRow row : r) {
        content.add(row.getCells().stream().map(c -> c.getValue(String.class)).toList());
      }
    }

    assertThat(content, is(List.of(List.of("header1", "header3"), List.of("alpha", "charlie"),
        List.of("golf", "india"))));
  }

  @Test
  public void activeFiltersTest() throws Exception {
    XlsxSpreadsheetFactory factory = new XlsxSpreadsheetFactory();

    ByteSource source = () -> Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("has-active-filters.xlsx");

    List<List<String>> content = new ArrayList<>();
    try (WorksheetReader r = factory.readActiveWorksheet(source)) {
      // This should throw an exception
      for (WorksheetRow row : r) {
        content.add(row.getCells().stream().map(c -> c.getValue(String.class)).toList());
      }
    }

    assertThat(content, is(List.of(List.of("header1", "header2", "header3"),
        List.of("delta", "echo", "foxtrot"), List.of("golf", "hotel", "india"))));
  }

  @Test
  public void activeSheetTest() throws Exception {
    XlsxSpreadsheetFactory factory = new XlsxSpreadsheetFactory();

    ByteSource source = () -> Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("has-multiple-sheets.xlsx");

    List<List<String>> content = new ArrayList<>();
    try (WorksheetReader r = factory.readActiveWorksheet(source)) {
      // This should throw an exception
      for (WorksheetRow row : r) {
        content.add(row.getCells().stream().map(c -> c.getValue(String.class)).toList());
      }
    }

    assertThat(content,
        is(List.of(List.of("delta", "echo", "foxtrot"), List.of("s101", "s102", "s103"),
            List.of("s104", "s105", "s106"), List.of("s107", "s108", "s109"))));
  }
}
