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
package com.sigpwned.tabular4j.exception;

import com.sigpwned.tabular4j.SpreadsheetException;

public class UnrecognizedFormatSpreadsheetException extends SpreadsheetException {
  private static final long serialVersionUID = 3832581778767224265L;

  private final String fileExtension;

  public UnrecognizedFormatSpreadsheetException(String fileExtension) {
    super("Unrecognized file extension: " + fileExtension);
    this.fileExtension = fileExtension;
  }

  /**
   * @return the fileExtension
   */
  public String getFileExtension() {
    return fileExtension;
  }
}
