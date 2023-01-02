package com.sigpwned.spreadsheet4j.csv.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import com.sigpwned.csv4j.CsvField;
import com.sigpwned.spreadsheet4j.csv.CsvFieldEncoder;

public final class CsvFieldEncoders {
  private CsvFieldEncoders() {}

  public static final CsvFieldEncoder BYTE =
      (v, c) -> v instanceof Byte ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder CHARACTER =
      (v, c) -> v instanceof Character ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder SHORT =
      (v, c) -> v instanceof Short ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder INTEGER =
      (v, c) -> v instanceof Integer ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder LONG =
      (v, c) -> v instanceof Long ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder BIG_INTEGER =
      (v, c) -> v instanceof BigInteger ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder FLOAT =
      (v, c) -> v instanceof Float ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder DOUBLE =
      (v, c) -> v instanceof Double ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder BIG_DECIMAL =
      (v, c) -> v instanceof BigDecimal ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder STRING =
      (v, c) -> v instanceof String ? CsvField.of(true, (String) v) : null;

  public static final CsvFieldEncoder BOOLEAN =
      (v, c) -> v instanceof Boolean ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder INSTANT =
      (v, c) -> v instanceof Instant ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder LOCAL_DATE =
      (v, c) -> v instanceof LocalDate ? CsvField.of(true, v.toString()) : null;

  public static final CsvFieldEncoder OFFSET_DATE_TIME =
      (v, c) -> v instanceof OffsetDateTime ? CsvField.of(true, v.toString()) : null;
}
