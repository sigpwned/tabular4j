/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2023 Andy Boothe
 * ====================================SECTION=====================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================LICENSE_END===================================
 */
package com.sigpwned.tabular4j.csv.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import com.sigpwned.chardet4j.Chardet;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.io.CharSource;

public final class MoreChardet {
  private MoreChardet() {}

  private static final int TEXT_BUF_LEN = 16384;

  /**
   * Decode the given byte source into a character source, using UTF-8 as the default encoding.
   * 
   * @param source the byte source
   * @return the character source
   * @throws IOException if an I/O error occurs
   * @see #decode(ByteSource, String)
   */
  public static CharSource decode(ByteSource source) throws IOException {
    return decode(source, "UTF-8");
  }

  /**
   * Decode the given byte source into a character source, preferring the declared encoding if
   * available. A prefix of the byte source is analyzed to determine the encoding, with the declared
   * encoding receiving a bump in priority.
   * 
   * @param source the byte source
   * @param declaredEncoding the declared encoding, or {@code null} if not available
   * @return the character source
   * @throws IOException if an I/O error occurs
   */
  public static CharSource decode(ByteSource source, String declaredEncoding) throws IOException {
    final byte[] buf;
    try (InputStream in = source.getInputStream()) {
      buf = in.readNBytes(TEXT_BUF_LEN);
    }

    if (buf.length == 0)
      return Reader::nullReader;

    final Charset charset = Chardet.detectCharset(buf, declaredEncoding).orElse(null);
    if (charset == null)
      throw new IOException("failed to detect charset");

    if (!isTextHeuristic(buf, charset))
      throw new IOException("data does not appear to be text");

    return source.asCharSource(charset);
  }

  /**
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/List_of_file_signatures">https://en.wikipedia.org/wiki/List_of_file_signatures</a>
   */
  private static final byte[] XLSX_MAGIC_BYTES = new byte[] {(byte) 0x50, (byte) 0x4B};

  /**
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/List_of_file_signatures">https://en.wikipedia.org/wiki/List_of_file_signatures</a>
   */
  private static final byte[] XLS_MAGIC_BYTES = new byte[] {(byte) 0xD0, (byte) 0xCF, (byte) 0x11,
      (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1};

  /**
   * Very basic heuristic for text that (a) checks blacklisted binary spreadsheet formats, then (b)
   * checks for alphanumeric text as the percentage of overall bytes.
   */
  private static boolean isTextHeuristic(byte[] preview, Charset detectedCharset) {
    for (byte[] blacklisted : new byte[][] {XLSX_MAGIC_BYTES, XLS_MAGIC_BYTES}) {
      if (isPrefix(blacklisted, preview)) {
        // This prefix is blacklisted. Not text!
        return false;
      }
    }

    final AtomicInteger texty = new AtomicInteger(0);
    final AtomicInteger total = new AtomicInteger(0);
    new String(preview, detectedCharset).codePoints().forEach(cp -> {
      if (Character.isLetterOrDigit(cp) || Character.isWhitespace(cp) || cp == '"' || cp == ',')
        texty.incrementAndGet();
      total.incrementAndGet();
    });

    return texty.get() > total.get() / 2;
  }

  /**
   * Returns {@code true} if the given needle is a prefix of the given haystack, or {@code false}
   * otherwise.
   */
  private static boolean isPrefix(byte[] needle, byte[] haystack) {
    return haystack.length >= needle.length
        && Arrays.compare(haystack, 0, needle.length, needle, 0, needle.length) == 0;
  }
}
