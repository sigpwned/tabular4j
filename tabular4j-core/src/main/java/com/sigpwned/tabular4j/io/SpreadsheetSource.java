/*-
 * =================================LICENSE_START==================================
 * tabular4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2024 Andy Boothe
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

import static java.util.Objects.requireNonNull;
import java.util.Optional;
import com.sigpwned.tabular4j.mime.MimeType;

public class SpreadsheetSource {
  private final ByteSource bytes;
  private final String fileExtension;
  private final MimeType mimeType;

  public SpreadsheetSource(ByteSource bytes, String fileExtension, MimeType mimeType) {
    this.bytes = requireNonNull(bytes);
    this.fileExtension = fileExtension;
    this.mimeType = mimeType;
  }

  public ByteSource getBytes() {
    return bytes;
  }

  public Optional<String> getFileExtension() {
    return Optional.ofNullable(fileExtension);
  }

  public Optional<MimeType> getMimeType() {
    return Optional.ofNullable(mimeType);
  }
}
