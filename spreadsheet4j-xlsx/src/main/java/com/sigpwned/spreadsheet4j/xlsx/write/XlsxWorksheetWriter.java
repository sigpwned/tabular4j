package com.sigpwned.spreadsheet4j.xlsx.write;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import com.sigpwned.spreadsheet4j.model.WorksheetCellDefinition;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;
import com.sigpwned.spreadsheet4j.xlsx.XlsxConfigRegistry;

public class XlsxWorksheetWriter implements WorksheetWriter {
  private final XlsxConfigRegistry config;
  private final XSSFSheet worksheet;
  private final int sheetIndex;
  private int rowIndex;

  public XlsxWorksheetWriter(XlsxConfigRegistry config, XSSFSheet worksheet, int sheetIndex) {
    this.config = requireNonNull(config);
    this.worksheet = requireNonNull(worksheet);
    this.sheetIndex = sheetIndex;
  }

  @Override
  public int getSheetIndex() {
    return sheetIndex;
  }

  @Override
  public String getSheetName() {
    return getWorksheet().getSheetName();
  }

  @Override
  public void writeRow(List<WorksheetCellDefinition> cells) throws IOException {
    XSSFRow row = getWorksheet().createRow(rowIndex++);
    for (int i = 0; i < cells.size(); i++) {
      WorksheetCellDefinition ci = cells.get(i);

      if (ci.getValue() == null)
        continue;

      XSSFCell cell = row.createCell(i);

      getConfig().findValueMapperForType(ci.getValue().getType())
          .orElseThrow(() -> new IllegalStateException("no mapper for type"))
          .setValue(cell, ci.getValue().getValue());
    }
  }

  @Override
  public void close() throws IOException {
    // Meh
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
}
