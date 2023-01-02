package com.sigpwned.spreadsheet4j.csv.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import com.sigpwned.chardet4j.Chardet;
import com.sigpwned.csv4j.CsvField;
import com.sigpwned.csv4j.CsvFormat;
import com.sigpwned.csv4j.read.CsvReader;
import com.sigpwned.csv4j.write.CsvWriter;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.io.ByteSink;

public final class Csv {
  private Csv() {}

  public static final String WORKSHEET_NAME = "csv";

  public static final CsvFormat CSV = CsvFormat.of('"', '"', ',');

  public static final CsvFormat TSV = CsvFormat.of('"', '"', '\t');

  public static CsvReader decode(ByteSource source) throws IOException {
    CsvReader result = null;

    InputStream bytes = source.getInputStream();
    try {
      Reader chars = Chardet.decode(bytes, StandardCharsets.UTF_8);
      try {
        result = new CsvReader(Csv.CSV, new PushbackReader(chars, 2));
      } finally {
        if (result == null)
          chars.close();
      }
    } finally {
      if (result == null)
        bytes.close();
    }

    return result;
  }

  public static CsvWriter encode(ByteSink sink) throws IOException {
    CsvWriter result = null;

    OutputStream bytes = sink.getOutputStream();
    try {
      Writer chars = new OutputStreamWriter(bytes, StandardCharsets.UTF_8);
      try {
        result = new CsvWriter(Csv.CSV, chars);
      } finally {
        if (result == null)
          chars.close();
      }
    } finally {
      if (result == null)
        bytes.close();
    }

    return result;
  }

  public static boolean isNull(CsvField field) {
    return !field.isQuoted() && isBlank(field);
  }

  public static boolean isBlank(CsvField field) {
    return field.getText().isEmpty();
  }
}
