package com.sigpwned.tabular4j;

import java.io.IOException;

public class SpreadsheetException extends IOException {
  private static final long serialVersionUID = 267438827267942018L;

  public SpreadsheetException(String message, Throwable cause) {
    super(message, cause);
  }

  public SpreadsheetException(String message) {
    super(message);
  }
}
