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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.sigpwned.tabular4j.io.ByteSink;

public class ByteArrayByteSink implements ByteSink {
  private byte[] bytes;

  public ByteArrayByteSink() {}

  @Override
  public ByteArrayOutputStream getOutputStream() throws IOException {
    return new ByteArrayOutputStream() {
      @Override
      public void close() throws IOException {
        try {
          super.close();
        } finally {
          ByteArrayByteSink.this.bytes = this.toByteArray();
        }
      }
    };
  }

  public byte[] getBytes() {
    if (bytes == null)
      throw new IllegalStateException("bytes not set");
    return bytes;
  }
}
