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
package com.sigpwned.spreadsheet4j.type;

import java.lang.reflect.Type;

/**
 * Lifted with love from JDBI, released under Apache v2 License.
 */
public abstract class GenericType<T> {
  private final Type type;

  protected GenericType() {
    this.type = GenericTypes.findGenericParameter(getClass(), GenericType.class)
        .orElseThrow(() -> new UnsupportedOperationException("Missing generic type parameter."));
  }

  public final Type getType() {
    return type;
  }
}
