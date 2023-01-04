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
package com.sigpwned.tabular4j.type;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Lifted with love from JDBI, released under Apache v2 License.
 */
/* default */ class Unchecked {
  private Unchecked() {}

  public static interface CheckedSupplier<T> {
    public T get() throws Throwable;
  }

  public static <T> Supplier<T> supplier(CheckedSupplier<T> checkedSupplier) {
    return () -> {
      try {
        return checkedSupplier.get();
      } catch (Throwable t) {
        throw Sneaky.throwAnyway(t);
      }
    };
  }

  public static interface CheckedFunction<X, Y> {
    public Y apply(X x) throws Throwable;
  }

  public static <X, Y> Function<X, Y> function(CheckedFunction<X, Y> checkedFunction) {
    return x -> {
      try {
        return checkedFunction.apply(x);
      } catch (Throwable t) {
        throw Sneaky.throwAnyway(t);
      }
    };
  }
}
