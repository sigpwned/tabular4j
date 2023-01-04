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
package com.sigpwned.spreadsheet4j.filter;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.sigpwned.spreadsheet4j.model.WorksheetCell;
import com.sigpwned.spreadsheet4j.model.WorksheetReader;
import com.sigpwned.spreadsheet4j.model.WorksheetRow;
import com.sigpwned.spreadsheet4j.type.QualifiedType;

public class RectangularWorksheetReader implements WorksheetReader {
  private static final int NO_WIDTH = -1;

  private final WorksheetReader delegate;
  private int width;

  public RectangularWorksheetReader(WorksheetReader delegate) {
    this.delegate = requireNonNull(delegate);
    this.width = NO_WIDTH;
  }

  @Override
  public int getSheetIndex() {
    return getDelegate().getSheetIndex();
  }

  @Override
  public String getSheetName() {
    return getDelegate().getSheetName();
  }

  @Override
  public boolean isActive() {
    return getDelegate().isActive();
  }

  @Override
  public WorksheetRow readRow() throws IOException {
    WorksheetRow result;

    WorksheetRow row = getDelegate().readRow();
    if (row == null) {
      result = null;
    } else if (width == NO_WIDTH) {
      if (row != null)
        width = row.getCells().size();
      result = row;
    } else if (row.getCells().size() > width) {
      final List<WorksheetCell> cells = unmodifiableList(row.getCells().subList(0, width));
      result = new WorksheetRow() {
        @Override
        public int getRowIndex() {
          return row.getRowIndex();
        }

        @Override
        public List<WorksheetCell> getCells() {
          return cells;
        }
      };
    } else if (row.getCells().size() < width) {
      List<WorksheetCell> cs = new ArrayList<>(row.getCells());
      while (cs.size() < width) {
        final int columnIndex = cs.size();
        cs.add(new WorksheetCell() {
          @Override
          public int getColumnIndex() {
            return columnIndex;
          }

          @Override
          public <T> T getValue(QualifiedType<T> qualifiedType) {
            return null;
          }
        });
      }
      final List<WorksheetCell> cells = unmodifiableList(cs);
      result = new WorksheetRow() {
        @Override
        public int getRowIndex() {
          return row.getRowIndex();
        }

        @Override
        public List<WorksheetCell> getCells() {
          return cells;
        }
      };
    } else {
      result = row;
    }

    return result;
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
