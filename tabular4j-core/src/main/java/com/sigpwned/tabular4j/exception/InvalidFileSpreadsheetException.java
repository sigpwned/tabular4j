package com.sigpwned.tabular4j.exception;

import com.sigpwned.tabular4j.SpreadsheetException;

public class InvalidFileSpreadsheetException extends SpreadsheetException {
  private static final long serialVersionUID = -1706981842975384906L;

  public InvalidFileSpreadsheetException() {
    super("Invalid file");
  }

  public InvalidFileSpreadsheetException(Throwable cause) {
    super("Invalid file", cause);
  }
}
