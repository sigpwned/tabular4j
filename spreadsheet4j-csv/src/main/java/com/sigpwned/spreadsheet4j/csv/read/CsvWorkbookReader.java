package com.sigpwned.spreadsheet4j.csv.read;

import java.io.IOException;
import java.util.List;
import com.sigpwned.spreadsheet4j.csv.CsvConfigRegistry;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.io.CharSource;
import com.sigpwned.spreadsheet4j.model.WorkbookReader;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.util.MoreChardet;

public class CsvWorkbookReader implements WorkbookReader {
  private final CsvConfigRegistry config;
  private final CharSource source;

  public CsvWorkbookReader(CsvConfigRegistry config, ByteSource source) throws IOException {
    this(config, MoreChardet.decode(source));
  }

  public CsvWorkbookReader(CsvConfigRegistry config, CharSource source) throws IOException {
    this.config = config;
    this.source = source;
  }

  @Override
  public List<WorksheetReader> getWorksheets() {
    return List.<WorksheetReader>of(new CsvWorksheetReader(getConfig(), getSource()));
  }

  @Override
  public void close() throws IOException {
    // NOP
  }

  /**
   * @return the config
   */
  public CsvConfigRegistry getConfig() {
    return config;
  }

  /**
   * @return the source
   */
  private CharSource getSource() {
    return source;
  }
}
