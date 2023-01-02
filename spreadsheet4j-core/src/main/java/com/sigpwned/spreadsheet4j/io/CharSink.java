package com.sigpwned.spreadsheet4j.io;

import java.io.IOException;
import java.io.Writer;

@FunctionalInterface
public interface CharSink {
  public Writer getWriter() throws IOException;
}
