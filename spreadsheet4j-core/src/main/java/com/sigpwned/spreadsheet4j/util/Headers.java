/*-
 * =================================LICENSE_START==================================
 * spreadsheet4j-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
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
package com.sigpwned.spreadsheet4j.util;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Headers {
  /**
   * Names, in order of position.
   */
  private final List<String> names;

  /**
   * Name to position. If there are duplicates, then the lowest index wins. Iteration order is in
   * ascending order and will include all unique header names, but in the presence of duplicates, it
   * will not include all values.
   */
  private final LinkedHashMap<String, Integer> namesToIndex;

  /**
   * Position to name. Iteration order is in ascending order of position.
   */
  private final LinkedHashMap<Integer, String> indexToNames;

  public Headers(List<String> names) {
    this.names = unmodifiableList(names);
    this.indexToNames = IntStream.range(0, names.size()).boxed()
        .collect(toMap(Function.identity(), names::get, (a, b) -> a, LinkedHashMap::new));
    this.namesToIndex =
        indexToNames.entrySet().stream().collect(groupingBy(Map.Entry::getValue, LinkedHashMap::new,
            collectingAndThen(mapping(Map.Entry::getKey, reducing((a, b) -> a)), Optional::get)));
  }

  public OptionalInt findColumnIndex(String name) {
    return IntStream.range(0, names.size())
        .filter(i -> names.get(i).equals(name))
        .findFirst();
  }

  /**
   * @return the names
   */
  private List<String> getNames() {
    return names;
  }

  /**
   * @return the namesToIndex
   */
  private LinkedHashMap<String, Integer> getNamesToIndex() {
    return namesToIndex;
  }

  /**
   * @return the indexToNames
   */
  private LinkedHashMap<Integer, String> getIndexToNames() {
    return indexToNames;
  }

  public int size() {
    return getNames().size();
  }
}
