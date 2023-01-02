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
package com.sigpwned.spreadsheet4j.model;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface WorksheetReader extends AutoCloseable {
  public int getSheetIndex();

  public String getSheetName();
  
  public boolean isActive();

  /**
   * The returned {@link Iterator} may throw a {@link UncheckedIOException} if there is an
   * {@link IOException} during processing.
   */
  public WorksheetRow readRow() throws IOException;

  public default Iterator<WorksheetRow> iterator() {
    final WorksheetRow first;
    try {
      first = readRow();
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read row", e);
    }
    return new Iterator<>() {
      private WorksheetRow next = first;

      @Override
      public boolean hasNext() {
        return next != null;
      }

      @Override
      public WorksheetRow next() {
        WorksheetRow result = next;
        try {
          next = readRow();
        } catch (IOException e) {
          throw new UncheckedIOException("Failed to read row", e);
        }
        return result;
      }
    };
  }

  public default Stream<WorksheetRow> stream() {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(),
        Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED), false);
  }

  @Override
  public void close() throws IOException;
}
