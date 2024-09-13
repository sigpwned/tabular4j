/*-
 * =================================LICENSE_START==================================
 * tabular4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 - 2024 Andy Boothe
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
package com.sigpwned.tabular4j.mime;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class MimeType {
  /**
   * <p>
   * Parses a MIME type from a string. The string must be in the form of
   * {@code type/subtype; key=value; key=value; ...}, with the type and subtype being required and
   * the parameters being optional.
   * </p>
   * 
   * <p>
   * This method performs a liberal parse. That is, it is likely to accept strings that are not
   * strictly valid MIME types. It will accept all valid MIME types.
   * </p>
   * 
   * <p>
   * For detailed syntax, see <a href="https://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>, Section
   * 5.1, Syntax of the Content-Type Header Field.
   * </p>
   * 
   * @param s
   * @return
   */
  public static MimeType fromString(String s) {
    if (s.indexOf('\r') != -1 || s.indexOf('\n') != -1)
      throw new IllegalArgumentException("Invalid mime type: must not contain newline");

    String typePart, parameterPart;
    int semicolon = s.indexOf(';');
    if (semicolon == -1) {
      typePart = s;
      parameterPart = null;
    } else {
      typePart = s.substring(0, semicolon);
      parameterPart = s.substring(semicolon + 1);
    }

    int slash = typePart.indexOf('/');
    if (slash == -1)
      throw new IllegalArgumentException("Invalid mime type: " + s);
    String type = typePart.substring(0, slash).trim().toLowerCase();
    String subtype = typePart.substring(slash + 1).trim().toLowerCase();

    Map<String, String> parameters;
    if (parameterPart != null) {
      parameters = new LinkedHashMap<>(4);

      int start = 0;
      while (start < parameterPart.length()) {
        int index = start;
        boolean quoted = false;
        while (index < parameterPart.length()) {
          char c = parameterPart.charAt(index);
          if (c == '"') {
            quoted = !quoted;
          } else if (quoted) {
            // carry on
          } else if (c == ';') {
            break;
          }
          index = index + 1;
        }

        String pi = parameterPart.substring(start, index).trim();

        String key, value;
        int eq = pi.indexOf('=');
        if (eq == -1) {
          // If there's no value, just assign an empty string to the value.
          key = pi;
          value = "";
        } else {
          key = pi.substring(0, eq).trim();
          if (pi.startsWith("\"") && pi.endsWith("\"")) {
            // If we're quoted, we need to remove the quotes and unescape backslash.
            // We can't just remove the backslashes because one backslash might escape another.
            value = pi.substring(eq + 1, pi.length() - 1).trim();
            int bslash = value.indexOf('\\');
            while (bslash != -1) {
              value = value.substring(0, bslash) + value.substring(bslash + 1, value.length());
              bslash = value.indexOf('\\');
            }
          } else {
            // Easy peasy. Just trim the value.
            value = pi.substring(eq + 1).trim();
          }
          value = pi.substring(eq + 1).trim();
        }
        parameters.put(key.toLowerCase(), value);

        start = index + 1;
      }
    } else {
      parameters = emptyMap();
    }

    return new MimeType(type, subtype, parameters);
  }

  public static MimeType of(String type, String subtype) {
    return new MimeType(type, subtype, emptyMap());
  }

  public static MimeType of(String type, String subtype, Map<String, String> parameters) {
    return new MimeType(type, subtype, parameters);
  }

  private final String type;
  private final String subtype;
  private final Map<String, String> parameters;

  public MimeType(String type, String subtype, Map<String, String> parameters) {
    this.type = requireNonNull(type);
    this.subtype = requireNonNull(subtype);
    this.parameters = parameters != null ? unmodifiableMap(parameters) : emptyMap();

    if (type.isEmpty())
      throw new IllegalArgumentException("type must not be empty");
    if (subtype.isEmpty())
      throw new IllegalArgumentException("subtype must not be empty");
    if (type.equals("*") && !subtype.equals("*"))
      throw new IllegalArgumentException("subtype must be wildcard if type is wildcard");
  }

  public boolean accepts(MimeType that) {
    // Check if the type matches or is a wildcard.
    if (!this.type.equals("*") && !this.type.equals(that.type))
      return false;

    // Ensure that all parameters in this MimeType are present in that MimeType.
    for (Map.Entry<String, String> parameter : this.parameters.entrySet()) {
      if (!Objects.equals(parameter.getValue(), that.parameters.get(parameter.getKey())))
        return false;
    }

    // Check if the subtype matches or is a wildcard.
    if (this.subtype.equals("*") || this.subtype.equals(that.subtype)) {
      return true;
    }

    // Check if the type and parameters match, and our subtype accepts the suffix of the other
    // subtype, e.g., application/json accepts application/ld+json
    if (this.getTree().isEmpty() && this.getSuffix().isEmpty() && that.getSuffix().isPresent()
        && this.getSubtype().equals(that.getSuffix().get())) {
      return true;
    }

    return false;
  }

  public String getType() {
    return type;
  }

  public String getSubtype() {
    return subtype;
  }

  public Optional<String> getTree() {
    int index = subtype.indexOf('.');
    if (index == -1)
      return Optional.empty();
    return Optional.of(subtype.substring(0, index));
  }

  public Optional<String> getSuffix() {
    int index = subtype.lastIndexOf('+');
    if (index == -1)
      return Optional.empty();
    return Optional.of(subtype.substring(index + 1, subtype.length()));
  }

  public Optional<String> getCharset() {
    return Optional.ofNullable(parameters.get("charset"));
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public MimeType withCharset(String charset) {
    if (charset == null)
      throw new NullPointerException();
    return withParameter("charset", charset);
  }

  public MimeType withoutCharset() {
    return withParameter("charset", null);
  }

  public MimeType withParameter(String key, String value) {
    if (key == null)
      throw new NullPointerException();
    if (value == null)
      throw new NullPointerException();
    Map<String, String> parameters = new LinkedHashMap<>(this.parameters);
    parameters.put(key, value);
    return new MimeType(type, subtype, parameters);
  }

  public MimeType withoutParameter(String key) {
    if (key == null)
      throw new NullPointerException();
    Map<String, String> parameters = new LinkedHashMap<>(this.parameters);
    parameters.remove(key);
    return new MimeType(type, subtype, parameters);
  }

  public MimeType withoutParameters() {
    return new MimeType(type, subtype, emptyMap());
  }

  @Override
  public String toString() {
    String result = type + "/" + subtype;
    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      result = result + "; " + entry.getKey() + "=" + "\""
          + entry.getValue().replace("\\", "\\\\").replace("\"", "\\\\") + "\"";
    }
    return result;
  }

  @Override
  public int hashCode() {
    return Objects.hash(parameters, subtype, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MimeType other = (MimeType) obj;
    return Objects.equals(parameters, other.parameters) && Objects.equals(subtype, other.subtype)
        && Objects.equals(type, other.type);
  }
}
