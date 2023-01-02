package com.sigpwned.spreadsheet4j.csv.write;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.sigpwned.csv4j.CsvField;
import com.sigpwned.csv4j.CsvRecord;
import com.sigpwned.csv4j.write.CsvWriter;
import com.sigpwned.spreadsheet4j.csv.CsvConfigRegistry;
import com.sigpwned.spreadsheet4j.csv.util.Csv;
import com.sigpwned.spreadsheet4j.io.ByteSink;
import com.sigpwned.spreadsheet4j.io.CharSink;
import com.sigpwned.spreadsheet4j.model.WorksheetCellDefinition;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class CsvWorksheetWriter implements WorksheetWriter {
  private final CsvConfigRegistry config;
  private final CharSink sink;
  private CsvWriter delegate;

  public CsvWorksheetWriter(CsvConfigRegistry config, ByteSink sink) {
    this(config, sink.asCharSink(StandardCharsets.UTF_8));
  }

  public CsvWorksheetWriter(CsvConfigRegistry config, CharSink sink) {
    this.config = requireNonNull(config);
    this.sink = requireNonNull(sink);
  }

  @Override
  public int getSheetIndex() {
    return 0;
  }

  @Override
  public String getSheetName() {
    return Csv.WORKSHEET_NAME;
  }

  @Override
  public void writeRow(List<WorksheetCellDefinition> cells) throws IOException {
    if (delegate == null)
      delegate = new CsvWriter(getSink().getWriter());

    List<CsvField> fields = new ArrayList<>(cells.size());
    for (WorksheetCellDefinition cell : cells) {
      Optional<CsvField> maybeField;
      if (cell.getValue() != null) {
        maybeField =
            getConfig().getEncoders().stream().map(e -> e.toCsvField(cell.getValue(), getConfig()))
                .filter(Objects::nonNull).findFirst();
      } else {
        maybeField = Optional.of(CsvField.of(false, ""));
      }
      fields.add(maybeField
          .orElseThrow(() -> new IllegalArgumentException("Unsupported type " + cell.getValue())));
    }

    // We don't care about style at all
    delegate.writeNext(CsvRecord.of(fields));
  }

  @Override
  public void close() throws IOException {
    if (delegate != null)
      delegate.close();
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
