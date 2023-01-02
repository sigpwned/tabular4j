package com.sigpwned.spreadsheet4j.xlsx.write;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.sigpwned.spreadsheet4j.model.WorkbookWriter;
import com.sigpwned.spreadsheet4j.model.WorksheetCellStyle;
import com.sigpwned.spreadsheet4j.xlsx.XlsxConfigRegistry;

public class XlsxWorkbookWriter implements WorkbookWriter {
  private final XlsxConfigRegistry config;
  private final XSSFWorkbook workbook;

  public XlsxWorkbookWriter(XlsxConfigRegistry config, XSSFWorkbook workbook) {
    this.config = requireNonNull(config);
    this.workbook = requireNonNull(workbook);
  }

  @Override
  public WorksheetCellStyle getStyle(boolean bold, boolean italic, boolean underlined) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public XlsxWorksheetWriter getWorksheet(String sheetName) {
    XSSFSheet sheet = getWorkbook().createSheet(sheetName);
    int sheetIndex = getWorkbook().getSheetIndex(sheetName);
    return new XlsxWorksheetWriter(getConfig(), sheet, sheetIndex);
  }

  @Override
  public void close() throws IOException {
    getWorkbook().close();
  }

  /**
   * @return the workbook
   */
  private XSSFWorkbook getWorkbook() {
    return workbook;
  }

  /**
   * @return the config
   */
  private XlsxConfigRegistry getConfig() {
    return config;
  }
}
