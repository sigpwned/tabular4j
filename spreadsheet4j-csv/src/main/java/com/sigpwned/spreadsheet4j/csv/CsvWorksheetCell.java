package com.sigpwned.spreadsheet4j.csv;

import java.lang.reflect.Type;
import com.sigpwned.csv4j.CsvField;
import com.sigpwned.spreadsheet4j.csv.util.Csv;
import com.sigpwned.spreadsheet4j.model.WorksheetCell;

public class CsvWorksheetCell implements WorksheetCell {
  private final int columnIndex;
  private final CsvField field;
  private final CsvConfigRegistry config;

  public CsvWorksheetCell(int columnIndex, CsvField field, CsvConfigRegistry config) {
    this.columnIndex = columnIndex;
    this.field = field;
    this.config = config;
  }

  @Override
  public int getColumnIndex() {
    return columnIndex;
  }

  @Override
  public Object getValue(Type type) {
    Object result;
    if (Csv.isNull(getField())) {
      result = null;
    } else {
      result = getConfig().getDecoders().stream()
          .map(d -> d.fromCsvField(type, getField(), getConfig())).findFirst()
          .orElseThrow(() -> new IllegalArgumentException("Unsupported type " + type)).orElse(null);
    }
    return result;
  }

  /**
   * @return the field
   */
  private CsvField getField() {
    return field;
  }

  /**
   * @return the config
   */
  private CsvConfigRegistry getConfig() {
    return config;
  }
}
