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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Lifted with love from JDBI, released under Apache v2 License.
 */
/* default */ final class Sneaky {
  private Sneaky() {}

  /**
   * Will <b>always</b> throw an exception, so the caller should also always throw the dummy return
   * value to make sure the control flow remains clear.
   * 
   * Implements so-called "sneaky throws." This is not a best practice, but can be useful to wrap a
   * call that is declared as throwing a checked exception that you know doesn't in practice.
   */
  public static RuntimeException throwAnyway(Throwable t) {
    if (t instanceof Error) {
      throw (Error) t;
    }

    if (t instanceof RuntimeException) {
      throw (RuntimeException) t;
    }

    if (t instanceof IOException) {
      throw new UncheckedIOException((IOException) t);
    }

    if (t instanceof InterruptedException) {
      Thread.currentThread().interrupt();
    }

    if (t instanceof InvocationTargetException) {
      throw throwAnyway(t.getCause());
    }

    throw throwEvadingChecks(t);
  }

  @SuppressWarnings({"unchecked"})
  private static <E extends Throwable> E throwEvadingChecks(Throwable throwable) throws E {
    throw (E) throwable;
  }
}
