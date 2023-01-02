package com.sigpwned.spreadsheet4j.csv.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.sigpwned.spreadsheet4j.csv.impl.read.CsvReader;
import com.sigpwned.spreadsheet4j.csv.impl.write.CsvWriter;
import com.sigpwned.spreadsheet4j.csv.util.Csv;

public class CsvImplTest {
  @Test
  public void smokeTest() throws IOException {
    final StringWriter wbuf = new StringWriter();

    final List<CsvRow> expecteds = List.of(
        CsvRow.of(List.of(CsvField.of(true, "alpha"), CsvField.of(true, "bravo"),
            CsvField.of(true, "charlie"))),
        CsvRow.of(
            List.of(CsvField.of(true, "hello"), CsvField.of(true, "10"), CsvField.of(true, "5"))),
        CsvRow.of(
            List.of(CsvField.of(true, "world"), CsvField.of(true, ""), CsvField.of(false, ""))));

    try (final CsvWriter w = new CsvWriter(Csv.CSV, wbuf)) {
      for (CsvRow e : expecteds)
        w.writeRecord(e);
    }

    final StringReader rbuf = new StringReader(wbuf.toString());

    final List<CsvRow> observeds = new ArrayList<>();

    try (final CsvReader r = new CsvReader(Csv.CSV, rbuf)) {
      for (CsvRow o = r.readRecord(); o != null; o = r.readRecord())
        observeds.add(o);
    }

    assertThat(observeds, is(expecteds));
  }
}
