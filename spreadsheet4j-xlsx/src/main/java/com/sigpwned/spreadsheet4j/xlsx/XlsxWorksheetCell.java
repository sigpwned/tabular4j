package com.sigpwned.spreadsheet4j.xlsx;

import static java.util.Objects.requireNonNull;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import com.sigpwned.spreadsheet4j.model.WorksheetCell;
import com.sigpwned.spreadsheet4j.type.QualifiedType;

public class XlsxWorksheetCell implements WorksheetCell {
  private final XlsxConfigRegistry config;
  private final int columnIndex;

  /**
   * Excel does not materialize all "logical" cells. (For example, Excel might only store data for
   * cell 3 on line 5.) This value will be {@code null} for such non-materialized cells. (In the
   * previous example, the {@code WorksheetCell} instances for cells 0, 1, 2 on line 5 would have a
   * null value for this property.)
   */
  private final XSSFCell cell;

  public XlsxWorksheetCell(XlsxConfigRegistry config, int columnIndex, XSSFCell cell) {
    this.config = requireNonNull(config);
    this.columnIndex = columnIndex;
    this.cell = cell;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getValue(QualifiedType<T> type) {
    if (getCell() == null)
      return null;
    if (getCell().getCellType() == CellType.BLANK)
      return null;
    return (T) getConfig().findValueMapperForType(type).map(v -> v.getValue(getCell()))
        .orElseThrow(() -> new IllegalStateException("no mapper for type"));
  }

  /**
   * @return the config
   */
  public XlsxConfigRegistry getConfig() {
    return config;
  }

  @Override
  public int getColumnIndex() {
    return columnIndex;
  }

  /**
   * @return the cell
   */
  private XSSFCell getCell() {
    return cell;
  }
}
