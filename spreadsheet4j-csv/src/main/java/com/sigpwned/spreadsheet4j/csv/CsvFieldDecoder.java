package com.sigpwned.spreadsheet4j.csv;

import java.lang.reflect.Type;
import java.util.Optional;
import com.sigpwned.csv4j.CsvField;

@FunctionalInterface
public interface CsvFieldDecoder {
  public Optional<Object> fromCsvField(Type type, CsvField field, CsvConfigRegistry config);
}
