package com.sigpwned.spreadsheet4j.type;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Lifted with love from JDBI, released under Apache v2 License. Previously called
 * {@code AnnotationFactory}.
 */
public final class Annotations {
  private Annotations() {}

  public static <T extends Annotation> T create(Class<T> annotationType) {
    return create(annotationType, Collections.emptyMap());
  }

  public static <T extends Annotation> T create(Class<T> annotationType, Map<String, ?> values) {
    Arrays.stream(annotationType.getDeclaredMethods()).filter(m -> m.getDefaultValue() == null)
        .filter(m -> !values.containsKey(m.getName())).findAny().ifPresent(m -> {
          throw new IllegalArgumentException(String.format(
              "Cannot synthesize annotation @%s from %s.class because it has attribute '%s' without a default or specified value",
              annotationType.getSimpleName(), annotationType.getName(), m.getName()));
        });

    Class<?>[] interfaces = {annotationType};
    InvocationHandler invocationHandler = getInvocationHandler(annotationType, values);

    @SuppressWarnings("unchecked")
    T annotation = (T) Proxy.newProxyInstance(Optional.ofNullable(annotationType.getClassLoader())
        .orElseGet(Thread.currentThread()::getContextClassLoader), interfaces, invocationHandler);

    return annotation;
  }

  private static <T extends Annotation> InvocationHandler getInvocationHandler(
      Class<T> annotationType, Map<String, ?> values) {
    final Function<Method, Object> getValue = method -> Optional
        .<Object>ofNullable(values.get(method.getName())).orElseGet(method::getDefaultValue);
    final int hashCode = Arrays.stream(annotationType.getDeclaredMethods())
        .mapToInt(m -> memberHash(m.getName(), getValue.apply(m))).sum();
    final String toString = "@" + annotationType.getName() + "("
        + values.entrySet().stream().sorted(Comparator.comparing(Entry::getKey))
            .map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", "))
        + ")";
    return (proxy, method, args) -> {
      String name = method.getName();
      if ("annotationType".equals(name) && method.getParameterCount() == 0) {
        return annotationType;
      }

      if ("equals".equals(name) && method.getParameterCount() == 1
          && Object.class.equals(method.getParameterTypes()[0])) {
        Object arg = args[0];
        if (!(arg instanceof Annotation)) {
          return false;
        }
        Annotation that = (Annotation) arg;
        return annotationType.equals(that.annotationType())
            && valuesEqual(annotationType, proxy, that);
      }

      if ("hashCode".equals(name) && method.getParameterCount() == 0) {
        return hashCode;
      }

      if ("toString".equals(name) && method.getParameterCount() == 0) {
        return toString;
      }

      if (method.getDeclaringClass() == annotationType) {
        return getValue.apply(method);
      }

      throw new IllegalStateException(
          "Unknown method " + method + " for annotation type " + annotationType);
    };
  }

  private static int memberHash(String name, Object value) {
    return (127 * name.hashCode()) ^ valueHash(value);
  }

  private static int valueHash(Object value) {
    Class<? extends Object> valueClass = value.getClass();
    if (!valueClass.isArray()) {
      return value.hashCode();
    }

    return Unchecked.supplier(() -> (Integer) MethodHandles.publicLookup()
        .findStatic(Arrays.class, "hashCode", MethodType.methodType(int.class, valueClass))
        .invoke(value)).get();
  }

  private static <A extends Annotation> boolean valuesEqual(Class<A> annotationType, Object a,
      Object b) {
    for (Method m : annotationType.getDeclaredMethods()) {
      Function<Object, Object> invoker = Unchecked.function(m::invoke);
      Object valueA = invoker.apply(a);
      Object valueB = invoker.apply(b);
      if (valueA != null && valueB != null && valueA.getClass().isArray()
          && valueA.getClass().equals(valueB.getClass())
          && Boolean.FALSE.equals(Unchecked.supplier(() -> MethodHandles.publicLookup()
              .findStatic(Arrays.class, "equals",
                  MethodType.methodType(boolean.class, valueA.getClass(), valueB.getClass()))
              .invoke(a, b)).get())) {
        return false;
      }
      if (!Objects.equals(valueA, valueB)) {
        return false;
      }
    }
    return true;
  }
}
