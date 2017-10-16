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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.github.kvnxiao.jsonequals.JsonCompareResult;
import com.github.kvnxiao.jsonequals.JsonEquals;
import com.github.kvnxiao.jsonequals.JsonRoot;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;

public class ComparisonTest {

  private static final String TEST_FOLDER = "tests/";

  @Before
  public void enableDebug() {
    JsonEquals.setDebugMode(true);
  }

  @Test
  public void bookTest() throws IOException {
    String bookTestA = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "book_a.json")));
    String bookTestB = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "book_b.json")));

    JsonRoot jsonA = JsonRoot.from(bookTestA);
    JsonRoot jsonB = JsonRoot.from(bookTestB);
    JsonCompareResult result = jsonA.compareTo(jsonB);

    assertTrue(result.isEqual());
  }

  @Test
  public void simpleNotEqualsArrayTest() throws IOException {
    String arrayTestA = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "array_a.json")));
    String arrayTestB = new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "array_b.json")));

    JsonRoot jsonA = JsonRoot.from(arrayTestA);
    JsonRoot jsonB = JsonRoot.from(arrayTestB);
    JsonCompareResult result = jsonA.compareTo(jsonB);

    assertEquals(1, result.getInequalityCount());
    assertFalse(result.isEqual());
  }

  @Test
  public void multiArrayTest() throws IOException {
    String multiArrayTestA =
        new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multiarray_a.json")));
    String multiArrayTestB =
        new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multiarray_b.json")));

    JsonRoot jsonA = JsonRoot.from(multiArrayTestA);
    JsonRoot jsonB = JsonRoot.from(multiArrayTestB);
    JsonCompareResult result = jsonA.compareTo(jsonB);

    assertTrue(result.isEqual());
  }

  @Test
  public void multipleArrayObjectsTest() throws IOException {
    String rawA =
        new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multi_array_objects_a.json")));
    String rawB =
        new String(Files.readAllBytes(Paths.get(TEST_FOLDER + "multi_array_objects_b.json")));

    JsonRoot jsonA = JsonRoot.from(rawA);
    JsonRoot jsonB = JsonRoot.from(rawB);
    JsonCompareResult result = jsonA.compareTo(jsonB);

    assertTrue(result.isEqual());
  }
}
