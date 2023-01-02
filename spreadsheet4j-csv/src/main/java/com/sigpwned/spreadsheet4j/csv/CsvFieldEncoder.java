package com.sigpwned.spreadsheet4j.csv;

import com.sigpwned.csv4j.CsvField;

@FunctionalInterface
public interface CsvFieldEncoder {
  public CsvField toCsvField(Object value, CsvConfigRegistry config);
}
