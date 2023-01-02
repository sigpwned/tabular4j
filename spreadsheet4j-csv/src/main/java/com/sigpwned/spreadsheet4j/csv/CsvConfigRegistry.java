package com.sigpwned.spreadsheet4j.csv;

import static java.util.Collections.unmodifiableList;
import java.util.ArrayList;
import java.util.List;
import com.sigpwned.spreadsheet4j.csv.util.CsvFieldDecoders;
import com.sigpwned.spreadsheet4j.csv.util.CsvFieldEncoders;

public class CsvConfigRegistry {
  private final List<CsvFieldEncoder> encoders;
  private final List<CsvFieldDecoder> decoders;

  public CsvConfigRegistry() {
    this.encoders = new ArrayList<>();
    addFieldEncoderFirst(CsvFieldEncoders.BYTE);
    addFieldEncoderFirst(CsvFieldEncoders.CHARACTER);
    addFieldEncoderFirst(CsvFieldEncoders.SHORT);
    addFieldEncoderFirst(CsvFieldEncoders.INTEGER);
    addFieldEncoderFirst(CsvFieldEncoders.LONG);
    addFieldEncoderFirst(CsvFieldEncoders.BIG_INTEGER);
    addFieldEncoderFirst(CsvFieldEncoders.FLOAT);
    addFieldEncoderFirst(CsvFieldEncoders.DOUBLE);
    addFieldEncoderFirst(CsvFieldEncoders.BIG_DECIMAL);
    addFieldEncoderFirst(CsvFieldEncoders.INSTANT);
    addFieldEncoderFirst(CsvFieldEncoders.LOCAL_DATE);
    addFieldEncoderFirst(CsvFieldEncoders.OFFSET_DATE_TIME);
    addFieldEncoderFirst(CsvFieldEncoders.STRING);

    this.decoders = new ArrayList<>();
    addFieldDecoderFirst(CsvFieldDecoders.BYTE);
    addFieldDecoderFirst(CsvFieldDecoders.CHARACTER);
    addFieldDecoderFirst(CsvFieldDecoders.SHORT);
    addFieldDecoderFirst(CsvFieldDecoders.INTEGER);
    addFieldDecoderFirst(CsvFieldDecoders.LONG);
    addFieldDecoderFirst(CsvFieldDecoders.BIG_INTEGER);
    addFieldDecoderFirst(CsvFieldDecoders.FLOAT);
    addFieldDecoderFirst(CsvFieldDecoders.DOUBLE);
    addFieldDecoderFirst(CsvFieldDecoders.BIG_DECIMAL);
    addFieldDecoderFirst(CsvFieldDecoders.INSTANT);
    addFieldDecoderFirst(CsvFieldDecoders.LOCAL_DATE);
    addFieldDecoderFirst(CsvFieldDecoders.OFFSET_DATE_TIME);
    addFieldDecoderFirst(CsvFieldDecoders.STRING);
  }

  public List<CsvFieldEncoder> getEncoders() {
    return unmodifiableList(encoders);
  }

  public CsvConfigRegistry addFieldEncoderFirst(CsvFieldEncoder encoder) {
    if (encoder == null)
      throw new NullPointerException();
    encoders.add(0, encoder);
    return this;
  }

  public CsvConfigRegistry addFieldEncoderLast(CsvFieldEncoder encoder) {
    if (encoder == null)
      throw new NullPointerException();
    encoders.add(encoders.size(), encoder);
    return this;
  }

  public List<CsvFieldDecoder> getDecoders() {
    return unmodifiableList(decoders);
  }

  public CsvConfigRegistry addFieldDecoderFirst(CsvFieldDecoder decoder) {
    if (decoder == null)
      throw new NullPointerException();
    decoders.add(0, decoder);
    return this;
  }

  public CsvConfigRegistry addFieldDecoderLast(CsvFieldDecoder decoder) {
    if (decoder == null)
      throw new NullPointerException();
    decoders.add(decoders.size(), decoder);
    return this;
  }
}
