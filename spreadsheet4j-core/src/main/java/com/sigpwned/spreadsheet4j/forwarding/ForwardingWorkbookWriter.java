package com.sigpwned.spreadsheet4j.forwarding;

import java.io.IOException;
import com.sigpwned.spreadsheet4j.model.WorkbookWriter;
import com.sigpwned.spreadsheet4j.model.WorksheetCellStyle;
import com.sigpwned.spreadsheet4j.model.WorksheetWriter;

public class ForwardingWorkbookWriter implements WorkbookWriter {
  private final WorkbookWriter delegate;

  public ForwardingWorkbookWriter(WorkbookWriter delegate) {
    this.delegate = delegate;
  }

  /**
   * @param bold
   * @param italic
   * @param underlined
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorksheetCellStyleFactory#getStyle(boolean, boolean,
   *      boolean)
   */
  public WorksheetCellStyle getStyle(boolean bold, boolean italic, boolean underlined) {
    return getDelegate().getStyle(bold, italic, underlined);
  }

  /**
   * @param name
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorkbookWriter#getWorksheet(java.lang.String)
   */
  public WorksheetWriter getWorksheet(String name) {
    return getDelegate().getWorksheet(name);
  }

  /**
   * @throws IOException
   * @see com.sigpwned.spreadsheet4j.model.WorkbookWriter#close()
   */
  public void close() throws IOException {
    getDelegate().close();
  }

  private WorkbookWriter getDelegate() {
    return delegate;
  }
}
