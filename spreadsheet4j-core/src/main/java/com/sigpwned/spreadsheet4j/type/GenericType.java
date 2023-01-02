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
