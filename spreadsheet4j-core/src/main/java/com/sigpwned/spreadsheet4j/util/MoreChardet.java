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
