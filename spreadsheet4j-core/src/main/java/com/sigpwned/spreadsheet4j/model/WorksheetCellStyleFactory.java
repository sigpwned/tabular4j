package com.sigpwned.spreadsheet4j.model;

public interface WorksheetCellStyleFactory {
  public WorksheetCellStyle getStyle(boolean bold, boolean italic, boolean underlined);
}
