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
package com.github.kvnxiao.jsonequals;

import static com.github.kvnxiao.jsonequals.Constants.BEGIN_BRACKET;
import static com.github.kvnxiao.jsonequals.Constants.BEGIN_CURLY;

import java.util.Map;
import java.util.Set;
import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyElement;
import me.doubledutch.lazyjson.LazyObject;
import me.doubledutch.lazyjson.LazyType;

/** A class representing the root JSON element / file. */
public class JsonRoot {

  /** The root element of the JSON object, often denoted by the dollar sign '$'. */
  private final LazyElement root;
  /** The type of the root JSON element, either a JSON object or a JSON array */
  private final LazyType rootType;

  // ------------
  // Constructors
  // ------------

  private JsonRoot(LazyElement root) {
    this.root = root;
    this.rootType = root.getType();
  }

  private JsonRoot(String raw) {
    if (raw.startsWith(BEGIN_CURLY)) {
      this.rootType = LazyType.OBJECT;
      this.root = new LazyObject(raw);
    } else if (raw.startsWith(BEGIN_BRACKET)) {
      this.rootType = LazyType.ARRAY;
      this.root = new LazyArray(raw);
    } else {
      // This shouldn't happen unless the specified string is not a valid JSON string!
      // The root of a JSON node should always be either an object or array
      this.rootType = LazyType.NULL;
      this.root = null;
    }
  }

  /**
   * Creates a JsonRoot instance using the provided LazyElement as the root JSON element.
   *
   * @param root The LazyElement object representing the root JSON element
   * @return A new JsonRoot instance
   */
  public static JsonRoot from(LazyElement root) {
    return new JsonRoot(root);
  }

  /**
   * Creates a JsonRoot instance using the provided raw JSON string.
   *
   * @param raw The raw JSON string to parse
   * @return A new JsonRoot instance
   */
  public static JsonRoot from(String raw) {
    return new JsonRoot(raw);
  }

  // ------------------
  // Comparator Methods
  // ------------------

  /**
   * Compares this JsonRoot with another JsonRoot object. Checks for deep-equality, with optionally
   * provided fields to ignore from comparison, and "stale" fields to prune and remove before
   * comparison.
   *
   * @param other The other JsonRoot to compare to
   * @param ignoreFields Optional JSON fields to ignore from the comparison
   * @param pruneFields Optional JSON fields to prune before comparison
   * @return The json comparison result
   */
  public JsonCompareResult compareTo(
      JsonRoot other, Set<String> ignoreFields, Map<String, String> pruneFields) {
    if (other != null) {
      if (this.isRootObject() && other.isRootObject()) {
        // JSON object node
        return JsonEquals.ofType(LazyType.OBJECT)
            .withSource(this.getRoot())
            .withComparate(other.getRoot())
            .withIgnoreFields(ignoreFields)
            .withPruneFields(pruneFields)
            .compare();
      } else if (this.isRootArray() && other.isRootArray()) {
        // JSON array node
        return JsonEquals.ofType(LazyType.ARRAY)
            .withSource(this.getRoot())
            .withComparate(other.getRoot())
            .withIgnoreFields(ignoreFields)
            .withPruneFields(pruneFields)
            .compare();
      }
    }
    return null;
  }

  /**
   * Compares this JsonRoot with another JsonRoot object. Checks for deep-equality.
   *
   * @param other The other JsonRoot to compare to
   * @return The json comparison result
   */
  public JsonCompareResult compareTo(JsonRoot other) {
    return compareTo(other, null, null);
  }

  /**
   * Compares this JsonRoot with another JsonRoot object. Checks for deep-equality, with optionally
   * provided fields to ignore from comparison.
   *
   * @param other The other JsonRoot to compare to
   * @param ignoreFields Optional JSON fields to ignore from the comparison
   * @return The json comparison result
   */
  public JsonCompareResult compareToWithIgnore(JsonRoot other, Set<String> ignoreFields) {
    return compareTo(other, ignoreFields, null);
  }

  /**
   * Compares this JsonRoot with another JsonRoot object. Checks for deep-equality, with optionally
   * provided "stale" fields to prune and remove before comparison.
   *
   * @param other The other JsonRoot to compare to
   * @param pruneFields Optional JSON fields to prune before comparison
   * @return The json comparison result
   */
  public JsonCompareResult compareToWithPrune(JsonRoot other, Map<String, String> pruneFields) {
    return compareTo(other, null, pruneFields);
  }

  // -----------------
  // Getters / Setters
  // -----------------

  /**
   * Gets the LazyElement root of this JsonRoot.
   *
   * @return The root JSON element as a LazyElement type
   */
  public LazyElement getRoot() {
    return this.root;
  }

  /**
   * Checks if the root JSON element is a JSON object.
   *
   * @return true if the root JSON element is a JSON object, false otherwise
   */
  public boolean isRootObject() {
    return this.rootType == LazyType.OBJECT;
  }

  /**
   * Checks if the root JSON element is a JSON array.
   *
   * @return true if the root JSON element is a JSON array, false otherwise
   */
  public boolean isRootArray() {
    return this.rootType == LazyType.ARRAY;
  }

  // ---------
  // Overrides
  // ---------

  /**
   * Converts the root JSON element into string form.
   *
   * @return The root JSON element in string form
   */
  @Override
  public String toString() {
    return this.root.toString();
  }
}
