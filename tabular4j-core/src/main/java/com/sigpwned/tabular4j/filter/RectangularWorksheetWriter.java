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
package com.sigpwned.tabular4j.filter;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.sigpwned.tabular4j.model.WorksheetCellDefinition;
import com.sigpwned.tabular4j.model.WorksheetWriter;

public class RectangularWorksheetWriter implements WorksheetWriter {
  private static final int NO_WIDTH = -1;

  private final WorksheetWriter delegate;
  private int width;

  public RectangularWorksheetWriter(WorksheetWriter delegate) {
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
  public void writeRow(List<WorksheetCellDefinition> cells) throws IOException {
    if (width == NO_WIDTH) {
      width = cells.size();
    } else if (cells.size() > width) {
      cells = cells.subList(0, width);
    } else if (cells.size() < width) {
      List<WorksheetCellDefinition> cs = new ArrayList<>(cells);
      while (cs.size() < width)
        cs.add(WorksheetCellDefinition.ofValue(null));
      cells = cs;
    }
    getDelegate().writeRow(cells);
  }

  @Override
  public void close() throws IOException {
    getDelegate().close();
  }

  /**
   * @return the delegate
   */
  private WorksheetWriter getDelegate() {
    return delegate;
  }
}
