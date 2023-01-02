package com.sigpwned.spreadsheet4j.xlsx;

import static java.util.Collections.unmodifiableList;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.sigpwned.spreadsheet4j.type.GenericType;
import com.sigpwned.spreadsheet4j.type.QualifiedType;
import com.sigpwned.spreadsheet4j.xlsx.util.CoreXlsxValueMapperFactory;

public class XlsxConfigRegistry {
  private final List<XlsxValueMapperFactory> valueMapperFactories;

  public XlsxConfigRegistry() {
    this.valueMapperFactories = new ArrayList<>();
    addValueMapperLast(CoreXlsxValueMapperFactory.INSTANCE);
  }

  public void addValueMapperFirst(XlsxValueMapperFactory valueMapperFactory) {
    valueMapperFactories.add(0, valueMapperFactory);
  }

  public void addValueMapperLast(XlsxValueMapperFactory valueMapperFactory) {
    valueMapperFactories.add(valueMapperFactories.size(), valueMapperFactory);
  }

  public Optional<XlsxValueMapper> findValueMapperForType(Class<?> klass) {
    return findValueMapperForType(QualifiedType.of(klass));
  }

  public Optional<XlsxValueMapper> findValueMapperForType(Type type) {
    return findValueMapperForType(QualifiedType.of(type));
  }

  public Optional<XlsxValueMapper> findValueMapperForType(GenericType<?> genericType) {
    return findValueMapperForType(QualifiedType.of(genericType));
  }

  public Optional<XlsxValueMapper> findValueMapperForType(QualifiedType<?> type) {
    return getValueMapperFactories().stream()
        .flatMap(f -> f.buildValueMapper(type, XlsxConfigRegistry.this).stream()).findFirst();
  }

  /**
   * @return the mapperFactories
   */
  public List<XlsxValueMapperFactory> getValueMapperFactories() {
    return unmodifiableList(valueMapperFactories);
  }
}
