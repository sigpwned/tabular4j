package com.sigpwned.spreadsheet4j.xlsx.util;

import static java.util.Collections.unmodifiableMap;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.poi.xssf.usermodel.XSSFCell;
import com.sigpwned.spreadsheet4j.type.QualifiedType;
import com.sigpwned.spreadsheet4j.xlsx.XlsxConfigRegistry;
import com.sigpwned.spreadsheet4j.xlsx.XlsxValueMapper;
import com.sigpwned.spreadsheet4j.xlsx.XlsxValueMapperFactory;

public class CoreXlsxValueMapperFactory implements XlsxValueMapperFactory {
  public static final CoreXlsxValueMapperFactory INSTANCE = new CoreXlsxValueMapperFactory();

  private static final Map<Type, XlsxValueMapper> VALUE_MAPPERS;
  static {
    Map<Type, XlsxValueMapper> valueMappers = new HashMap<>();

    // Boolean Primitives
    valueMappers.put(Boolean.class, new XlsxValueMapper() {
      @Override
      public void setValue(XSSFCell cell, Object value) {
        Boolean x = (Boolean) value;
        cell.setCellValue(x.booleanValue());
      }

      @Override
      public Object getValue(XSSFCell cell) {
        Boolean x = cell.getBooleanCellValue();
        return x;
      }
    });

    // Integer primitives
    valueMappers.put(Byte.class, new XlsxValueMapper() {
      @Override
      public void setValue(XSSFCell cell, Object value) {
        Byte x = (Byte) value;
        cell.setCellValue(x.doubleValue());
      }

      @Override
      public Object getValue(XSSFCell cell) {
        Double x = cell.getNumericCellValue();
        return x.byteValue();
      }
    });
    valueMappers.put(Short.class, new XlsxValueMapper() {
      @Override
      public void setValue(XSSFCell cell, Object value) {
        Short x = (Short) value;
        cell.setCellValue(x.doubleValue());
      }

      @Override
      public Object getValue(XSSFCell cell) {
        Double x = cell.getNumericCellValue();
        return x.shortValue();
      }
    });
    valueMappers.put(Integer.class, new XlsxValueMapper() {
      @Override
      public void setValue(XSSFCell cell, Object value) {
        Integer x = (Integer) value;
        cell.setCellValue(x.doubleValue());
      }

      @Override
      public Object getValue(XSSFCell cell) {
        Double x = cell.getNumericCellValue();
        return x.intValue();
      }
    });
    valueMappers.put(Long.class, new XlsxValueMapper() {
      @Override
      public void setValue(XSSFCell cell, Object value) {
        Long x = (Long) value;
        cell.setCellValue(x.doubleValue());
      }

      @Override
      public Object getValue(XSSFCell cell) {
        Double x = cell.getNumericCellValue();
        return x.longValue();
      }
    });

    // Floating point primitives
    valueMappers.put(Float.class, new XlsxValueMapper() {
      @Override
      public void setValue(XSSFCell cell, Object value) {
        Float x = (Float) value;
        cell.setCellValue(x.floatValue());
      }

      @Override
      public Object getValue(XSSFCell cell) {
        Double x = cell.getNumericCellValue();
        return x.floatValue();
      }
    });
    valueMappers.put(Double.class, new XlsxValueMapper() {
      @Override
      public void setValue(XSSFCell cell, Object value) {
        Double x = (Double) value;
        cell.setCellValue(x.doubleValue());
      }

      @Override
      public Object getValue(XSSFCell cell) {
        Double x = cell.getNumericCellValue();
        return x.doubleValue();
      }
    });

    // Text primitives
    valueMappers.put(Character.class, new XlsxValueMapper() {
      @Override
      public void setValue(XSSFCell cell, Object value) {
        Character x = (Character) value;
        cell.setCellValue(x.toString());
      }

      @Override
      public Object getValue(XSSFCell cell) {
        String s = cell.getStringCellValue();
        if (s.isEmpty())
          throw new IllegalStateException("empty");
        return s.charAt(0);
      }
    });
    valueMappers.put(String.class, new XlsxValueMapper() {
      @Override
      public void setValue(XSSFCell cell, Object value) {
        String x = (String) value;
        cell.setCellValue(x);
      }

      @Override
      public Object getValue(XSSFCell cell) {
        String s = cell.getStringCellValue();
        return s;
      }
    });

    VALUE_MAPPERS = unmodifiableMap(valueMappers);
  }

  @Override
  public Optional<XlsxValueMapper> buildValueMapper(QualifiedType<?> type,
      XlsxConfigRegistry registry) {
    if (!type.getQualifiers().isEmpty())
      return Optional.empty();
    return Optional.ofNullable(VALUE_MAPPERS.get(type.getType()));
  }
}
