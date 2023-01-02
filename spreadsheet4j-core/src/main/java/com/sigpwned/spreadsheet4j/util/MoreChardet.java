package com.sigpwned.spreadsheet4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import com.sigpwned.chardet4j.Chardet;
import com.sigpwned.spreadsheet4j.io.ByteSource;
import com.sigpwned.spreadsheet4j.io.CharSource;

public class MoreChardet {
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
            result = Chardet.decode(in, StandardCharsets.UTF_8);
            charset = Charset.forName(result.getEncoding());
          }
        } finally {
          if (result == null)
            in.close();
        }

        return result;
      }
    };
  }
}
