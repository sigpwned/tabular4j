package com.sigpwned.spreadsheet4j.csv.write;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.sigpwned.spreadsheet4j.csv.CsvConfigRegistry;
import com.sigpwned.spreadsheet4j.io.ByteSink;
import com.sigpwned.spreadsheet4j.io.CharSink;
import com.sigpwned.spreadsheet4j.model.WorkbookWriter;
import com.sigpwned.spreadsheet4j.model.WorksheetCellStyle;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class CsvWorkbookWriter implements WorkbookWriter {
  private final CsvConfigRegistry config;
  private final CharSink sink;

  public CsvWorkbookWriter(CsvConfigRegistry config, ByteSink sink) {
    this(config, sink.asCharSink(StandardCharsets.UTF_8));
  }

  public CsvWorkbookWriter(CsvConfigRegistry config, CharSink sink) {
    this.config = requireNonNull(config);
    this.sink = requireNonNull(sink);
  }

  @Override
  public WorksheetWriter getWorksheet(String name) {
    // We don't care about the name.
    return new CsvWorksheetWriter(getConfig(), getSink());
  }

  /**
   * CSV has no embedded styling, so the "closest" style is always none.
   */
  private static final WorksheetCellStyle STYLE = new WorksheetCellStyle() {
    @Override
    public boolean isBold() {
      return false;
    }

    @Override
    public boolean isItalic() {
      return false;
    }

    @Override
    public boolean isUnderlined() {
      return false;
    }
  };

  @Override
  public WorksheetCellStyle getStyle(boolean bold, boolean italic, boolean underlined) {
    return STYLE;
  }

  @Override
  public void close() throws IOException {
    // Nothing to do.
  }

  /**
   * @return the config
   */
  public CsvConfigRegistry getConfig() {
    return config;
  }

  /**
   * @return the sink
   */
  private CharSink getSink() {
    return sink;
  }
}
