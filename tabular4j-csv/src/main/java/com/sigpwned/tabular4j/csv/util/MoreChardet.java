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

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import com.sigpwned.chardet4j.Chardet;
import com.sigpwned.tabular4j.io.ByteSource;
import com.sigpwned.tabular4j.io.CharSource;

public class MoreChardet {
  private static final int TEXT_BUF_LEN = 16384;

  public static CharSource decode(ByteSource source) {
    return new CharSource() {
      private Charset charset;

      @Override
      public Reader getReader() throws IOException {
        InputStreamReader result = null;

        InputStream in = source.getInputStream();
        try {
          if (charset != null) {
            result = new InputStreamReader(in, charset);
          } else {
            byte[] buf = in.readNBytes(TEXT_BUF_LEN);
            if (buf.length == 0)
              throw new EOFException();

            result = Chardet.decode(new SequenceInputStream(new ByteArrayInputStream(buf), in),
                StandardCharsets.UTF_8);
            charset = Charset.forName(result.getEncoding());

            if (!isTextHeuristic(buf, charset))
              throw new IOException("not text");
          }
        } finally {
          if (result == null)
            in.close();
        }

        return result;
      }
    };
  }

  /**
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/List_of_file_signatures">https://en.wikipedia.org/wiki/List_of_file_signatures</a>
   */
  public static final byte[] XLSX_MAGIC_BYTES = new byte[] {(byte) 0x50, (byte) 0x4B};

  /**
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/List_of_file_signatures">https://en.wikipedia.org/wiki/List_of_file_signatures</a>
   */
  public static final byte[] XLS_MAGIC_BYTES = new byte[] {(byte) 0xD0, (byte) 0xCF, (byte) 0x11,
      (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1};

  /**
   * Very basic heuristic for text that (a) checks blacklisted binary spreadsheet formats, then (b)
   * checks for alphanumeric text as the percentage of overall bytes.
   */
  private static boolean isTextHeuristic(byte[] preview, Charset detectedCharset) {
    for (byte[] blacklisted : new byte[][] {XLSX_MAGIC_BYTES, XLS_MAGIC_BYTES}) {
      if (preview.length >= blacklisted.length && Arrays.compare(blacklisted, 0, blacklisted.length,
          preview, 0, blacklisted.length) == 0) {
        // This prefix is blacklisted. Not text!
        return false;
      }
    }

    final AtomicInteger alphanum = new AtomicInteger(0);
    final AtomicInteger total = new AtomicInteger(0);
    new String(preview, detectedCharset).codePoints().forEach(cp -> {
      if (Character.isLetterOrDigit(cp))
        alphanum.incrementAndGet();
      total.incrementAndGet();
    });

    return alphanum.get() > total.get() / 2;
  }
}
