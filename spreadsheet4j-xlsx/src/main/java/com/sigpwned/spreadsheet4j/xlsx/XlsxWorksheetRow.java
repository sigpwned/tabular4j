package com.sigpwned.spreadsheet4j.xlsx;

import static java.util.Collections.unmodifiableList;
import java.util.List;
import com.sigpwned.spreadsheet4j.model.WorksheetCell;
import com.sigpwned.spreadsheet4j.model.WorksheetRow;

public class XlsxWorksheetRow implements WorksheetRow {
  private final int rowIndex;
  private final List<XlsxWorksheetCell> cells;

  public XlsxWorksheetRow(int rowIndex, List<XlsxWorksheetCell> cells) {
    if (rowIndex < 0)
      throw new IllegalArgumentException("rowIndex must not be negative");
    if (cells == null)
      throw new NullPointerException();
    this.rowIndex = rowIndex;
    this.cells = unmodifiableList(cells);
  }

  @Override
  public int getRowIndex() {
    return rowIndex;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<WorksheetCell> getCells() {
    return (List) cells;
  }
}
