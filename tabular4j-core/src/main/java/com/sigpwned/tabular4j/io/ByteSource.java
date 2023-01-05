/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
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
package com.sigpwned.tabular4j.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import com.sigpwned.tabular4j.io.source.ByteArrayByteSource;
import com.sigpwned.tabular4j.io.source.FileByteSource;
import com.sigpwned.tabular4j.io.source.UrlByteSource;

@FunctionalInterface
public interface ByteSource {
  public static FileByteSource ofFile(File file) {
    return new FileByteSource(file);
  }

  public static ByteArrayByteSource ofBytes(byte[] bytes) {
    return new ByteArrayByteSource(bytes);
  }

  public static UrlByteSource ofUrl(URL url) {
    return new UrlByteSource(url);
  }

  public InputStream getInputStream() throws IOException;

  public default CharSource asCharSource(Charset charset) throws IOException {
    return () -> new InputStreamReader(getInputStream(), charset);
  }
}
