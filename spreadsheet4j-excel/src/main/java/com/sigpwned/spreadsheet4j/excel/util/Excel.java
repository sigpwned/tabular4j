package com.sigpwned.spreadsheet4j.excel.util;

public final class Excel {
  private Excel() {}

  public static final byte[] XLSX_MAGIC_BYTES = new byte[] {(byte) 0x50, (byte) 0x4B};

  public static final byte[] XLS_MAGIC_BYTES = new byte[] {(byte) 0xD0, (byte) 0xCF, (byte) 0x11,
      (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1};
}
