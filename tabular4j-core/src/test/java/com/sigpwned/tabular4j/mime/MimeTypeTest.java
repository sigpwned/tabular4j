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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class MimeTypeTest {
  @Test
  public void givenValidMimeType_whenToString_thenGetExpectedResult() {
    assertThat(MimeType.of("application", "json").toString(), is("application/json"));
    assertThat(MimeType.of("application", "vnd.ms-excel.spreadsheet").toString(),
        is("application/vnd.ms-excel.spreadsheet"));
    assertThat(MimeType.of("application", "ld+json").withCharset("utf-8").toString(),
        is("application/ld+json; charset=\"utf-8\""));
  }

  @Test
  public void givenValidMimeType_whenEquals_thenGetExpectedResult() {
    assertThat(MimeType.of("application", "json"), is(MimeType.of("application", "json")));
    assertThat(MimeType.of("application", "json").withCharset("utf-8"),
        is(MimeType.of("application", "json").withCharset("utf-8")));
    assertThat(MimeType.of("application", "json").withCharset("utf-8"),
        not(is(MimeType.of("application", "json"))));
  }

  @Test
  public void givenValidMimeType_whenAccepts_thenGetExpectedResult() {
    // equal without parameters
    assertThat(MimeType.of("application", "json").accepts(MimeType.of("application", "json")),
        is(true));

    // equal with parameters
    assertThat(MimeType.of("application", "json").withCharset("utf-8")
        .accepts(MimeType.of("application", "json").withCharset("utf-8")), is(true));

    // type and subtype match, with RHS having more parameters
    assertThat(MimeType.of("application", "json")
        .accepts(MimeType.of("application", "json").withCharset("utf-8")), is(true));

    // type and subtype match, with LHS having more parameters
    assertThat(MimeType.of("application", "json").withCharset("utf-8")
        .accepts(MimeType.of("application", "json")), is(false));

    // LHS wildcard subtype
    assertThat(MimeType.of("application", "*").accepts(MimeType.of("application", "json")),
        is(true));

    // RHS wildcard subtype
    assertThat(MimeType.of("application", "json").accepts(MimeType.of("application", "*")),
        is(false));

    // LHS full wildcard
    assertThat(MimeType.of("*", "*").accepts(MimeType.of("application", "json")), is(true));

    // RHS full wildcard
    assertThat(MimeType.of("application", "json").accepts(MimeType.of("*", "*")), is(false));

    // RHS suffix LHS without tree without suffix
    assertThat(MimeType.of("application", "json").accepts(MimeType.of("application", "ld+json")),
        is(true));

    // RHS suffix LHS with tree without suffix
    assertThat(MimeType.of("application", "vnd.example.json")
        .accepts(MimeType.of("application", "ld+json")), is(false));

    // RHS suffix LHS without tree with suffix
    assertThat(
        MimeType.of("application", "text+json").accepts(MimeType.of("application", "ld+json")),
        is(false));

    // RHS suffix LHS with tree with suffix
    assertThat(MimeType.of("application", "vnd.example.text+json")
        .accepts(MimeType.of("application", "ld+json")), is(false));

    // LHS suffix
    assertThat(MimeType.of("application", "ld+json").accepts(MimeType.of("application", "json")),
        is(false));
  }
}
