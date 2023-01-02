package com.sigpwned.spreadsheet4j.xlsx;

import java.lang.reflect.Type;
import java.util.Optional;
import com.sigpwned.spreadsheet4j.type.GenericType;
import com.sigpwned.spreadsheet4j.type.QualifiedType;

public interface XlsxValueMapperFactory {
  public default Optional<XlsxValueMapper> buildValueMapper(Class<?> type,
      XlsxConfigRegistry registry) {
    return buildValueMapper(QualifiedType.of(type), registry);
  }

  public default Optional<XlsxValueMapper> buildValueMapper(Type type,
      XlsxConfigRegistry registry) {
    return buildValueMapper(QualifiedType.of(type), registry);
  }

  public default Optional<XlsxValueMapper> buildValueMapper(GenericType<?> type,
      XlsxConfigRegistry registry) {
    return buildValueMapper(QualifiedType.of(type), registry);
  }

  public Optional<XlsxValueMapper> buildValueMapper(QualifiedType<?> type,
      XlsxConfigRegistry registry);

}
