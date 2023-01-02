package com.sigpwned.spreadsheet4j.csv.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import com.sigpwned.spreadsheet4j.csv.CsvFieldDecoder;

public final class CsvFieldDecoders {
  private CsvFieldDecoders() {}

  public static final CsvFieldDecoder BYTE = (t, f, c) -> t.equals(Byte.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : Byte.parseByte(f.getText()))
      : null;

  public static final CsvFieldDecoder CHARACTER = (t, f, c) -> t.equals(Character.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : f.getText().charAt(0))
      : null;

  public static final CsvFieldDecoder SHORT = (t, f, c) -> t.equals(Short.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : Short.parseShort(f.getText()))
      : null;

  public static final CsvFieldDecoder INTEGER = (t, f, c) -> t.equals(Integer.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : Integer.parseInt(f.getText()))
      : null;

  public static final CsvFieldDecoder LONG = (t, f, c) -> t.equals(Long.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : Long.parseLong(f.getText()))
      : null;

  public static final CsvFieldDecoder BIG_INTEGER = (t, f, c) -> t.equals(BigInteger.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : new BigInteger(f.getText()))
      : null;

  public static final CsvFieldDecoder FLOAT = (t, f, c) -> t.equals(Float.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : Float.parseFloat(f.getText()))
      : null;

  public static final CsvFieldDecoder DOUBLE = (t, f, c) -> t.equals(Double.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : Double.parseDouble(f.getText()))
      : null;

  public static final CsvFieldDecoder BIG_DECIMAL = (t, f, c) -> t.equals(BigDecimal.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : new BigDecimal(f.getText()))
      : null;

  public static final CsvFieldDecoder STRING =
      (t, f, c) -> t.equals(String.class) ? Optional.ofNullable(Csv.isBlank(f) ? null : f.getText())
          : null;

  public static final CsvFieldDecoder BOOLEAN = (t, f, c) -> t.equals(Boolean.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : Boolean.parseBoolean(f.getText()))
      : null;

  public static final CsvFieldDecoder INSTANT = (t, f, c) -> t.equals(Instant.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : Instant.parse(f.getText()))
      : null;

  public static final CsvFieldDecoder LOCAL_DATE = (t, f, c) -> t.equals(LocalDate.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : LocalDate.parse(f.getText()))
      : null;

  public static final CsvFieldDecoder OFFSET_DATE_TIME = (t, f, c) -> t.equals(OffsetDateTime.class)
      ? Optional.ofNullable(Csv.isBlank(f) ? null : OffsetDateTime.parse(f.getText()))
      : null;
}
