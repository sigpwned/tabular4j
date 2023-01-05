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
package com.sigpwned.tabular4j.test;

import static java.util.stream.Collectors.toList;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.IntStream;
import com.sigpwned.tabular4j.model.WorksheetCell;
import com.sigpwned.tabular4j.model.WorksheetReader;
import com.sigpwned.tabular4j.model.WorksheetRow;
import com.sigpwned.tabular4j.type.QualifiedType;

public class TestWorksheetReader implements WorksheetReader {
  private final ListIterator<List<String>> iterator;

  public TestWorksheetReader(ListIterator<List<String>> iterator) {
    this.iterator = iterator;
  }

  @Override
  public int getSheetIndex() {
    return 0;
  }

  @Override
  public String getSheetName() {
    return "test";
  }

  @Override
  public boolean isActive() {
    return true;
  }

  private static final QualifiedType<String> STRING = QualifiedType.of(String.class);

  @Override
  public WorksheetRow readRow() throws IOException {
    if (!iterator.hasNext())
      return null;
    final List<String> record = iterator.next();
    final int rowIndex = iterator.previousIndex();
    return new WorksheetRow() {
      @Override
      public int getRowIndex() {
        return rowIndex;
      }

      @Override
      public List<WorksheetCell> getCells() {
        return IntStream.range(0, record.size()).mapToObj(i -> new WorksheetCell() {
          @Override
          public int getColumnIndex() {
            return i;
          }

          @Override
          @SuppressWarnings("unchecked")
          public <T> T getValue(QualifiedType<T> qualifiedType) {
            if (!qualifiedType.equals(STRING))
              throw new ClassCastException();
            return (T) record.get(i);
          }
        }).collect(toList());
      }
    };
  }

  @Override
  public void close() throws IOException {
    // NOP
  }
}
