package com.sigpwned.tabular4j.exception;

import com.sigpwned.tabular4j.SpreadsheetException;

public class UnrecognizedFormatSpreadsheetException extends SpreadsheetException {
  private static final long serialVersionUID = 3832581778767224265L;

  private final String fileExtension;

  public UnrecognizedFormatSpreadsheetException(String fileExtension) {
    super("Unrecognized file extension: " + fileExtension);
    this.fileExtension = fileExtension;
  }

  /**
   * @return the fileExtension
   */
  public String getFileExtension() {
    return fileExtension;
  }
}
