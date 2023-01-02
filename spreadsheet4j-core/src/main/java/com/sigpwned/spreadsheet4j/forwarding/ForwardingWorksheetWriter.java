package com.sigpwned.spreadsheet4j.forwarding;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.List;
import com.sigpwned.spreadsheet4j.model.WorksheetCellDefinition;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class ForwardingWorksheetWriter implements WorksheetWriter {
  private final WorksheetWriter delegate;

  public ForwardingWorksheetWriter(WorksheetWriter delegate) {
    this.delegate = requireNonNull(delegate);
  }

  /**
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorksheetWriter#getSheetIndex()
   */
  public int getSheetIndex() {
    return getDelegate().getSheetIndex();
  }

  /**
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorksheetWriter#getSheetName()
   */
  public String getSheetName() {
    return getDelegate().getSheetName();
  }

  /**
   * @param cells
   * @throws IOException
   * @see com.sigpwned.spreadsheet4j.model.WorksheetWriter#writeRow(java.util.List)
   */
  public void writeRow(List<WorksheetCellDefinition> cells) throws IOException {
    getDelegate().writeRow(cells);
  }

  /**
   * @throws IOException
   * @see com.sigpwned.spreadsheet4j.model.WorksheetWriter#close()
   */
  public void close() throws IOException {
    getDelegate().close();
  }

  private WorksheetWriter getDelegate() {
    return delegate;
  }
}
