package com.sigpwned.spreadsheet4j.xlsx;

import org.apache.poi.xssf.usermodel.XSSFCell;

public interface XlsxValueMapper {
  public void setValue(XSSFCell cell, Object value);

  public Object getValue(XSSFCell cell);
}
