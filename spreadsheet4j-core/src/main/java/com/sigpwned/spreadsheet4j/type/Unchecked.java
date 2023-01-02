package com.sigpwned.spreadsheet4j.type;

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
