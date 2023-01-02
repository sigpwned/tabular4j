package com.sigpwned.spreadsheet4j.type;

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
