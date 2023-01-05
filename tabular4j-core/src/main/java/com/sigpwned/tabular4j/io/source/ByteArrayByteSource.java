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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.sigpwned.tabular4j.io.ByteSource;

public class ByteArrayByteSource implements ByteSource {
  private final byte[] bytes;

  public ByteArrayByteSource(byte[] bytes) {
    this.bytes = bytes;
  }

  @Override
  public ByteArrayInputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(getBytes());
  }

  /**
   * @return the url
   */
  private byte[] getBytes() {
    return bytes;
  }
}
