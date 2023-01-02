package com.sigpwned.spreadsheet4j.forwarding;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorksheetRow;

public class ForwardingWorksheetReader implements WorksheetReader {
  private final WorksheetReader delegate;

  public ForwardingWorksheetReader(WorksheetReader delegate) {
    this.delegate = requireNonNull(delegate);
  }

  /**
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorksheetReader#getSheetIndex()
   */
  public int getSheetIndex() {
    return getDelegate().getSheetIndex();
  }

  /**
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorksheetReader#getSheetName()
   */
  public String getSheetName() {
    return getDelegate().getSheetName();
  }

  /**
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorksheetReader#isActive()
   */
  public boolean isActive() {
    return getDelegate().isActive();
  }

  /**
   * @return
   * @throws IOException
   * @see com.sigpwned.spreadsheet4j.model.WorksheetReader#readRow()
   */
  public WorksheetRow readRow() throws IOException {
    return getDelegate().readRow();
  }

  /**
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorksheetReader#iterator()
   */
  public Iterator<WorksheetRow> iterator() {
    return getDelegate().iterator();
  }

  /**
   * @return
   * @see com.sigpwned.spreadsheet4j.model.WorksheetReader#stream()
   */
  public Stream<WorksheetRow> stream() {
    return getDelegate().stream();
  }

  /**
   * @throws IOException
   * @see com.sigpwned.spreadsheet4j.model.WorksheetReader#close()
   */
  public void close() throws IOException {
    getDelegate().close();
  }

  private WorksheetReader getDelegate() {
    return delegate;
  }
}
