package com.sigpwned.spreadsheet4j.xlsx.read;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorksheetRow;
import com.sigpwned.spreadsheet4j.xlsx.XlsxConfigRegistry;
import com.sigpwned.spreadsheet4j.xlsx.XlsxWorksheetCell;
import com.sigpwned.spreadsheet4j.xlsx.XlsxWorksheetRow;

public class XlsxWorksheetReader implements WorksheetReader {
  private final XlsxConfigRegistry config;
  private final XSSFSheet worksheet;
  private final int sheetIndex;
  private final boolean active;
  private int rowIndex;

  public XlsxWorksheetReader(XlsxConfigRegistry config, XSSFSheet worksheet, int sheetIndex,
      boolean active) {
    this.config = requireNonNull(config);
    this.worksheet = requireNonNull(worksheet);
    this.active = active;
    this.sheetIndex = sheetIndex;
  }

  @Override
  public WorksheetRow readRow() throws IOException {
    if (getWorksheet().getFirstRowNum() == -1)
      return null;

    if (rowIndex > getWorksheet().getLastRowNum())
      return null;

    if (rowIndex < getWorksheet().getFirstRowNum()) {
      WorksheetRow result = new XlsxWorksheetRow(rowIndex, List.of());
      rowIndex = rowIndex + 1;
      return result;
    }

    XSSFRow row = getWorksheet().getRow(rowIndex);

    if (row.getFirstCellNum() == -1) {
      WorksheetRow result = new XlsxWorksheetRow(rowIndex, List.of());
      rowIndex = rowIndex + 1;
      return result;
    }

    List<XlsxWorksheetCell> cells = new ArrayList<>();
    for (int i = 0; i < row.getFirstCellNum(); i++)
      cells.add(new XlsxWorksheetCell(getConfig(), i, null));
    for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++)
      cells.add(new XlsxWorksheetCell(getConfig(), i, row.getCell(i)));

    WorksheetRow result = new XlsxWorksheetRow(rowIndex, cells);

    rowIndex = rowIndex + 1;

    return result;
  }

  /**
   * @return the config
   */
  public XlsxConfigRegistry getConfig() {
    return config;
  }

  /**
   * @return the worksheet
   */
  private XSSFSheet getWorksheet() {
    return worksheet;
  }

  @Override
  public int getSheetIndex() {
    return sheetIndex;
  }

  @Override
  public String getSheetName() {
    return getWorksheet().getSheetName();
  }

  /**
   * @return the active
   */
  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public void close() throws IOException {
    // Meh
  }
}
