/*-
 * =================================LICENSE_START==================================
 * tabular4j-core
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
package com.sigpwned.tabular4j.io.sink;

import static java.util.Objects.requireNonNull;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.sigpwned.tabular4j.io.ByteSink;

public class UrlByteSink implements ByteSink {
  public static final String POST = "POST";

  private final URL url;

  public UrlByteSink(URL url) {
    this.url = requireNonNull(url);
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    HttpURLConnection cn = (HttpURLConnection) getUrl().openConnection();
    cn.setRequestMethod(POST);
    cn.setDoOutput(true);
    cn.setDoInput(true);
    configure(cn);
    OutputStream out = cn.getOutputStream();
    InputStream in = cn.getInputStream();
    return new FilterOutputStream(out) {
      @Override
      public void close() throws IOException {
        try {
          super.close();
        } finally {
          try {
            try {
              in.transferTo(OutputStream.nullOutputStream());
            } finally {
              in.close();
            }
          } finally {
            cn.disconnect();
          }
        }
      }
    };
  }

  /**
   * Configuration hook
   */
  protected void configure(HttpURLConnection connection) {}

  /**
   * @return the url
   */
  public URL getUrl() {
    return url;
  }
}
