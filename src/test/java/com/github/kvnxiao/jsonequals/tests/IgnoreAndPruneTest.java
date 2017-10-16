/*
 *  Copyright 2017 Ze Hao (Kevin) Xiao
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.kvnxiao.jsonequals.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.kvnxiao.jsonequals.JsonCompareResult;
import com.github.kvnxiao.jsonequals.JsonEquals;
import com.github.kvnxiao.jsonequals.JsonRoot;

import org.junit.Before;
import org.junit.Test;

public class IgnoreAndPruneTest {

  private static final String ignoreAndPruneA = "tests/ignore_prune_a.json";
  private static final String ignoreAndPruneB = "tests/ignore_prune_b.json";
  private static final String pruneA = "tests/prune_a.json";
  private static final String pruneb = "tests/prune_b.json";

  @Before
  public void enableDebug() {
    JsonEquals.setDebugMode(true);
  }

  @Test
  public void pruneTest() throws IOException {
    String rawA = new String(Files.readAllBytes(Paths.get(pruneA)));
    String rawB = new String(Files.readAllBytes(Paths.get(pruneb)));
    JsonRoot jsonA = JsonRoot.from(rawA);
    JsonRoot jsonB = JsonRoot.from(rawB);

    Map<String, String> pruneFields = new HashMap<>();
    pruneFields.put("$.array[*]:id.isValid", "false");

    JsonCompareResult result = jsonA.compareToWithPrune(jsonB, pruneFields);
    result.getInequalityMessages().forEach(System.out::println);

    assertTrue(result.isEqual());
  }

  @Test
  public void ignoreAndPruneTest() throws IOException {
    String rawA = new String(Files.readAllBytes(Paths.get(ignoreAndPruneA)));
    String rawB = new String(Files.readAllBytes(Paths.get(ignoreAndPruneB)));
    JsonRoot jsonA = JsonRoot.from(rawA);
    JsonRoot jsonB = JsonRoot.from(rawB);

    Set<String> ignoreFields = new HashSet<>();
    Map<String, String> pruneFields = new HashMap<>();

    ignoreFields.add("$[*].data.last_updated");
    pruneFields.put("$[*].data.identities[*]:installed", "false");

    JsonCompareResult result = jsonA.compareTo(jsonB, ignoreFields, pruneFields);
    result.getInequalityMessages().forEach(System.out::println);

    assertTrue(result.isEqual());
  }
}
