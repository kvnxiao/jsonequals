/*
 * JsonEquals - A flexible deep-equality comparator for JSON files
 * Copyright (C) 2017 Ze Hao Xiao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.kvnxiao.jsonequals.tests;

import com.github.kvnxiao.jsonequals.JsonCompareResult;
import com.github.kvnxiao.jsonequals.JsonEquals;
import com.github.kvnxiao.jsonequals.JsonRoot;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

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
