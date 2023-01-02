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
package com.sigpwned.spreadsheet4j.util;

import java.util.Objects;

public class Header {
  public static Header of(int index, String name) {
    return new Header(index, name);
  }

  private final int index;
  private final String name;

  public Header(int index, String name) {
    if (index < 0)
      throw new IllegalArgumentException("index must not be negative");
    if (name == null)
      throw new NullPointerException();
    this.index = index;
    this.name = name;
  }

  /**
   * @return the index
   */
  public int getIndex() {
    return index;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(index, name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Header other = (Header) obj;
    return index == other.index && Objects.equals(name, other.name);
  }

  @Override
  public String toString() {
    return "Header [index=" + index + ", name=" + name + "]";
  }
}
