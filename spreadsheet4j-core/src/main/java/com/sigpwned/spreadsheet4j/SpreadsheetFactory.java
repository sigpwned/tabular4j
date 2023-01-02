package com.sigpwned.spreadsheet4j;

import java.io.IOException;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.io.ByteSink;
import com.sigpwned.spreadsheet4j.model.WorkbookReader;
import com.sigpwned.spreadsheet4j.model.WorkbookWriter;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public interface SpreadsheetFactory {
  public WorkbookReader readWorkbook(ByteSource source) throws IOException;

  public WorksheetReader readActiveWorksheet(ByteSource source) throws IOException;

  public WorkbookWriter writeWorkbook(ByteSink sink) throws IOException;

  public WorksheetWriter writeActiveWorksheet(ByteSink sink) throws IOException;
}
