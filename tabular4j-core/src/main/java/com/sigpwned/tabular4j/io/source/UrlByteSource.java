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
package com.sigpwned.tabular4j.io.source;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.sigpwned.tabular4j.io.ByteSource;

public class UrlByteSource implements ByteSource {
  private final URL url;

  public UrlByteSource(URL url) {
    this.url = requireNonNull(url);
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return getUrl().openStream();
  }

  /**
   * @return the url
   */
  public URL getUrl() {
    return url;
  }
}
