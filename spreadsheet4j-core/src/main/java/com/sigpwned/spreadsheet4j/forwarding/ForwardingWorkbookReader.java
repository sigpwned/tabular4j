package com.sigpwned.spreadsheet4j.forwarding;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.List;
import com.sigpwned.spreadsheet4j.model.WorkbookReader;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;

public class ForwardingWorkbookReader implements WorkbookReader {
  private final WorkbookReader delegate;

  public ForwardingWorkbookReader(WorkbookReader delegate) {
    this.delegate = requireNonNull(delegate);
  }

  @Override
  public List<WorksheetReader> getWorksheets() {
    return getDelegate().getWorksheets();
  }

  @Override
  public void close() throws IOException {
    getDelegate().close();
  }

  /**
   * @return the delegate
   */
  private WorkbookReader getDelegate() {
    return delegate;
  }
}
