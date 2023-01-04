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
package com.sigpwned.tabular4j.model;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import java.io.EOFException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import com.sigpwned.tabular4j.filter.RectangularWorksheetReader;

public class TabularWorksheetReader implements Iterable<TabularWorksheetRow>, AutoCloseable {
  private final WorksheetReader delegate;
  private final List<String> columnNames;

  public TabularWorksheetReader(WorksheetReader delegate) throws IOException {
    this.delegate = new RectangularWorksheetReader(delegate);
    this.columnNames = Optional.ofNullable(getDelegate().readRow()).map(
        r -> r.getCells().stream().map(c -> c.getValue(String.class)).collect(toUnmodifiableList()))
        .orElseThrow(EOFException::new);
    if (getColumnNames().isEmpty())
      throw new IOException("no columns");
  }

  public int getSheetIndex() {
    return getDelegate().getSheetIndex();
  }

  public String getSheetName() {
    return getDelegate().getSheetName();
  }

  public boolean isActive() {
    return getDelegate().isActive();
  }

  public List<String> getColumnNames() {
    return columnNames;
  }

  public String getColumnName(int index) {
    return getColumnNames().get(index);
  }

  public OptionalInt findColumnName(String columnName) {
    return IntStream.range(0, getColumnNames().size())
        .filter(i -> getColumnName(i).equals(columnName)).findFirst();
  }

  /**
   * The returned {@link Iterator} may throw a {@link UncheckedIOException} if there is an
   * {@link IOException} during processing.
   */
  public TabularWorksheetRow readRow() throws IOException {
    WorksheetRow row = getDelegate().readRow();
    return Optional.ofNullable(row)
        .map(r -> new TabularWorksheetRow(r.getRowIndex(),
            IntStream.range(0, r.size())
                .mapToObj(i -> new TabularWorksheetCell(r.getCell(i), getColumnName(i)))
                .collect(toList())))
        .orElse(null);
  }

  public Iterator<TabularWorksheetRow> iterator() {
    IOException problem;
    TabularWorksheetRow first;
    try {
      first = readRow();
      problem = null;
    } catch (IOException e) {
      first = null;
      problem = e;
    }

    final IOException theproblem = problem;
    final TabularWorksheetRow thefirst = first;

    return new Iterator<>() {
      private TabularWorksheetRow next = thefirst;
      private IOException cause = theproblem;

      @Override
      public boolean hasNext() {
        return next != null;
      }

      @Override
      public TabularWorksheetRow next() {
        if (cause != null)
          throw new UncheckedIOException("Failed to read row", cause);

        TabularWorksheetRow result = next;
        try {
          next = readRow();
        } catch (IOException e) {
          cause = e;
          throw new UncheckedIOException("Failed to read row", cause);
        }

        return result;
      }
    };
  }

  public Stream<TabularWorksheetRow> stream() {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(),
        Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED), false);
  }

  @Override
  public void close() throws IOException {
    getDelegate().close();
  }

  /**
   * @return the delegate
   */
  private WorksheetReader getDelegate() {
    return delegate;
  }
}
