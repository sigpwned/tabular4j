package com.sigpwned.spreadsheet4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.sigpwned.spreadsheet4j.io.ByteSink;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.model.WorkbookReader;
import com.sigpwned.spreadsheet4j.model.WorkbookWriter;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class SpreadsheetFactory {
  private final List<SpreadsheetFormatFactory> formats;

  public SpreadsheetFactory() {
    this.formats = new ArrayList<>();
  }

  public void addFormat(SpreadsheetFormatFactory format) {
    getFormats().add(format);
  }

  public WorkbookReader readWorkbook(ByteSource source) throws IOException {
    WorkbookReader result = null;
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        result = format.readWorkbook(source);
      } catch (IOException e) {
        // TODO Should we only catch a specific format mismatch exception here?
        // This is OK. It's just not in the given format.
      }
    }
    if (result == null)
      throw new IOException("Unrecognized file format");
    return result;
  }

  public WorksheetReader readActiveWorksheet(ByteSource source) throws IOException {
    WorksheetReader result = null;
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        result = format.readActiveWorksheet(source);
      } catch (IOException e) {
        // TODO Should we only catch a specific format mismatch exception here?
        // This is OK. It's just not in the given format.
      }
    }
    if (result == null)
      throw new IOException("Unrecognized file format");
    return result;
  }

  public WorkbookWriter writeWorkbook(ByteSink sink) throws IOException {
    WorkbookWriter result = null;
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        result = format.writeWorkbook(sink);
      } catch (IOException e) {
        // TODO Should we only catch a specific format mismatch exception here?
        // This is OK. It's just not in the given format.
      }
    }
    if (result == null)
      throw new IOException("Unrecognized file format");
    return result;
  }

  public WorksheetWriter writeActiveWorksheet(ByteSink sink) throws IOException {
    WorksheetWriter result = null;
    for (SpreadsheetFormatFactory format : getFormats()) {
      try {
        result = format.writeActiveWorksheet(sink);
      } catch (IOException e) {
        // TODO Should we only catch a specific format mismatch exception here?
        // This is OK. It's just not in the given format.
      }
    }
    if (result == null)
      throw new IOException("Unrecognized file format");
    return result;
  }

  /**
   * @return the formats
   */
  private List<SpreadsheetFormatFactory> getFormats() {
    return formats;
  }
}
