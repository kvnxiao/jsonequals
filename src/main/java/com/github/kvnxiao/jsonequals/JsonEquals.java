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
package com.github.kvnxiao.jsonequals;

import static com.github.kvnxiao.jsonequals.Constants.BEGIN_BRACKET;
import static com.github.kvnxiao.jsonequals.Constants.END_BRACKET;
import static com.github.kvnxiao.jsonequals.Constants.NODE_SEPARATOR;
import static com.github.kvnxiao.jsonequals.Constants.PREDICATE_SEPARATOR;
import static com.github.kvnxiao.jsonequals.Constants.ROOT_NAME;
import static com.github.kvnxiao.jsonequals.Constants.SEPARATOR_REGEX;
import static com.github.kvnxiao.jsonequals.Constants.WILDCARD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyElement;
import me.doubledutch.lazyjson.LazyObject;
import me.doubledutch.lazyjson.LazyType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A builder class used to compare two JSON elements. */
public class JsonEquals {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonEquals.class);
  private static boolean debugMode = false;

  private final LazyType rootType;
  private LazyElement source = null;
  private LazyElement comparate = null;
  private Map<String, String> pruneFields;
  private Set<String> ignoreFields;
  private List<String> successMessages;
  private List<String> inequalityMessages;

  private JsonEquals(LazyType rootType) {
    this.rootType = rootType;
    this.successMessages = new ArrayList<>();
    this.inequalityMessages = new ArrayList<>();
  }

  /**
   * Returns a new JsonEquals builder instance with the specified LazyType, being either for JSON
   * objects or JSON arrays.
   *
   * @param rootType The type of the JSON element, specified as either an object or array
   * @return A new JsonEquals instance
   */
  public static JsonEquals ofType(LazyType rootType) {
    return new JsonEquals(rootType);
  }

  /**
   * Returns a new JsonEquals builder instance between two JSON objects.
   *
   * @param source The JSON object source
   * @param comparate The JSON object to be compared to
   * @return A new JsonEquals instance with the specified source and comparate JSON objects
   */
  public static JsonEquals between(LazyObject source, LazyObject comparate) {
    return new JsonEquals(LazyType.OBJECT).withSource(source).withComparate(comparate);
  }

  /**
   * Returns a new JsonEquals builder instance between two JSON arrays.
   *
   * @param source The JSON array source
   * @param comparate The JSON array to be compared to
   * @return A new JsonEquals instance with the specified source and comparate JSON arrays
   */
  public static JsonEquals between(LazyArray source, LazyArray comparate) {
    return new JsonEquals(LazyType.ARRAY).withSource(source).withComparate(comparate);
  }

  /**
   * Specifies the source JSON element.
   *
   * @param source The JSON element source
   * @return The JsonEquals instance
   */
  public JsonEquals withSource(LazyElement source) {
    this.source = source;
    return this;
  }

  /**
   * Specifies the JSON element comparate.
   *
   * @param comparate The JSON element to compare to
   * @return The JsonEquals instance
   */
  public JsonEquals withComparate(LazyElement comparate) {
    this.comparate = comparate;
    return this;
  }

  /**
   * Specifies the optional fields to ignore from comparison.
   *
   * @param ignoreFields The set of JSON node paths to ignore from comparison
   * @return The JsonEquals instance
   */
  public JsonEquals withIgnoreFields(Set<String> ignoreFields) {
    this.ignoreFields = ignoreFields;
    return this;
  }

  /**
   * Specifies the optional fields to prune before comparison.
   *
   * @param pruneFields The predicate map of JSON node paths to expected values that need to be
   *     pruned before comparison
   * @return The JsonEquals instance
   */
  public JsonEquals withPruneFields(Map<String, String> pruneFields) {
    this.pruneFields = pruneFields;
    return this;
  }

  /**
   * Compares the source with the comparate and returns a JsonCompareResult.
   *
   * @return The result of the deep-equal comparison
   */
  public JsonCompareResult compare() {
    if (rootType == LazyType.OBJECT) {
      compareNode((LazyObject) source, (LazyObject) comparate);
    } else {
      compareNode((LazyArray) source, (LazyArray) comparate);
    }
    return JsonCompareResult.of(inequalityMessages.isEmpty(), successMessages, inequalityMessages);
  }

  /**
   * Compares two JSON objects, starting from the root level.
   *
   * @param a source JSON object
   * @param b comparate JSON object
   */
  public void compareNode(LazyObject a, LazyObject b) {
    compareNode(a, b, ROOT_NAME);
  }

  /**
   * Compares two JSON arrays, starting from the root level.
   *
   * @param a source JSON array
   * @param b comparate JSON array
   */
  public void compareNode(LazyArray a, LazyArray b) {
    compareNode(a, b, ROOT_NAME);
  }

  /**
   * Compares two JSON objects.
   *
   * @param a source JSON object
   * @param b comparate JSON object
   * @param currentPath The current JSON node path
   */
  public void compareNode(LazyObject a, LazyObject b, String currentPath) {
    if (pathIsIgnoreField(currentPath)) {
      return;
    }

    Set<String> fieldsA = a.keySet();
    Set<String> fieldsB = b.keySet();
    if (fieldsA.equals(fieldsB)) {
      for (String fieldName : fieldsA) {
        if (childIsObject(a, fieldName) && childIsObject(b, fieldName)) {
          compareNode(
              a.getJSONObject(fieldName),
              b.getJSONObject(fieldName),
              getChildPath(currentPath, fieldName));
        } else if (childIsArray(a, fieldName) && childIsArray(b, fieldName)) {
          compareNode(
              a.getJSONArray(fieldName),
              b.getJSONArray(fieldName),
              getChildPath(currentPath, fieldName));
        } else {
          compareValues(a, b, fieldName, getChildPath(currentPath, fieldName));
        }
      }
    } else {
      inequalityMessages.add(
          "JSON objects do not have the same child key names! " + fieldsA + " vs. " + fieldsB);
    }
  }

  /**
   * Compares two JSON arrays.
   *
   * @param a source JSON array
   * @param b comparate JSON array
   * @param currentPath The current JSON node path
   */
  public void compareNode(LazyArray a, LazyArray b, String currentPath) {
    if (pathIsIgnoreField(currentPath)) {
      return;
    }

    JsonChildren childrenA = getChildList(a);
    JsonChildren childrenB = getChildList(b);
    if (pruneFields != null && !pruneFields.isEmpty()) {
      prune(childrenA, currentPath, "source");
      prune(childrenB, currentPath, "comparate");
    }
    if (!childrenA.isEmpty() && !childrenB.isEmpty()) {
      if (childrenA.objectCount() == childrenB.objectCount()
          && childrenA.arrayCount() == childrenB.arrayCount()
          && childrenA.valueCount() == childrenB.valueCount()) {
        for (int i = 0; i < childrenA.size(); i++) {

          if (childrenA.getType(i) == JsonChildren.Type.OBJECT
              && childrenB.getType(i) == JsonChildren.Type.OBJECT) {
            // Compare child objects
            compareNode(
                childrenA.getObj(i),
                childrenB.getObj(i),
                currentPath + BEGIN_BRACKET + i + END_BRACKET);

          } else if (childrenA.getType(i) == JsonChildren.Type.ARRAY
              && childrenB.getType(i) == JsonChildren.Type.ARRAY) {
            // Compare child arrays
            compareNode(
                childrenA.getArr(i),
                childrenB.getArr(i),
                currentPath + BEGIN_BRACKET + i + END_BRACKET);

          } else if (childrenA.getType(i) == JsonChildren.Type.VALUE
              && childrenB.getType(i) == JsonChildren.Type.VALUE) {
            // Compare primitive values
            if (debugMode) {
              LOGGER.debug(
                  "Checking array value: {}", currentPath + BEGIN_BRACKET + i + END_BRACKET);
            }
            if (!childrenA.get(i).equals(childrenB.get(i))) {
              inequalityMessages.add(
                  currentPath
                      + " JSON array value expected to be "
                      + childrenA.get(i)
                      + " but got "
                      + childrenB.get(i));
            } else {
              successMessages.add(
                  currentPath + BEGIN_BRACKET + i + END_BRACKET + "==" + childrenA.get(i));
            }
          } else {
            inequalityMessages.add(
                currentPath
                    + BEGIN_BRACKET
                    + i
                    + END_BRACKET
                    + " types were not the same! Expected "
                    + childrenA.getType(i)
                    + " but got "
                    + childrenB.getType(i));
          }
        }
      } else {
        inequalityMessages.add(
            currentPath
                + " JSON array not equal in length! "
                + childrenA.size()
                + " vs "
                + childrenB.size());
      }
    }
  }

  /**
   * Compares the values at the end of the JSON hierarchy (i.e. a leaf node)
   *
   * @param a the value from the source
   * @param b the value from the comparate
   * @param fieldName the name of the JSON field which holds this value
   * @param currentPath The current JSON node path
   */
  public void compareValues(LazyObject a, LazyObject b, String fieldName, String currentPath) {
    if (pathIsIgnoreField(currentPath)) {
      return;
    }

    if (debugMode) {
      LOGGER.debug("Checking leaf object: {}", currentPath);
    }

    if (a.getType(fieldName) == LazyType.STRING && b.getType(fieldName) == LazyType.STRING) {
      if (a.getString(fieldName).equals(b.getString(fieldName))) {
        successMessages.add(currentPath + "==" + a.getString(fieldName));
      } else {
        logInequality(a.getString(fieldName), b.getString(fieldName), currentPath);
      }
    } else if (a.getType(fieldName) == LazyType.INTEGER
        && b.getType(fieldName) == LazyType.INTEGER) {
      if (a.getInt(fieldName) == b.getInt(fieldName)) {
        successMessages.add(currentPath + "==" + a.getString(fieldName));
      } else {
        logInequality(a.getString(fieldName), b.getString(fieldName), currentPath);
      }
    } else if (a.getType(fieldName) == LazyType.BOOLEAN
        && b.getType(fieldName) == LazyType.BOOLEAN) {
      if (a.getBoolean(fieldName) == b.getBoolean(fieldName)) {
        successMessages.add(currentPath + "==" + a.getString(fieldName));
      } else {
        logInequality(a.getString(fieldName), b.getString(fieldName), currentPath);
      }
    } else if (a.getType(fieldName) == LazyType.FLOAT && b.getType(fieldName) == LazyType.FLOAT) {
      if (a.getString(fieldName).equals(b.getString(fieldName))) {
        successMessages.add(currentPath + "==" + a.getString(fieldName));
      } else {
        logInequality(a.getString(fieldName), b.getString(fieldName), currentPath);
      }
    } else if (a.getType(fieldName) == LazyType.NULL && b.getType(fieldName) == LazyType.NULL) {
      successMessages.add(currentPath + "==" + a.getString(fieldName));
    } else {
      inequalityMessages.add(
          currentPath
              + " were not of the same type! Expected type "
              + a.getType(fieldName)
              + " but got type "
              + b.getType(fieldName));
    }
  }

  private JsonChildren getChildList(LazyArray parent) {
    JsonChildren jsonChildren = JsonChildren.create();
    for (int i = 0; i < parent.length(); i++) {
      switch (parent.getType(i)) {
        case OBJECT:
          jsonChildren.addChildObject(parent.getJSONObject(i));
          break;
        case ARRAY:
          jsonChildren.addChildArray(parent.getJSONArray(i));
          break;
        case STRING:
          jsonChildren.addChildValue(parent.getString(i));
          break;
        case INTEGER:
          jsonChildren.addChildValue(parent.getInt(i));
          break;
        case BOOLEAN:
          jsonChildren.addChildValue(parent.getBoolean(i));
          break;
        case FLOAT:
          jsonChildren.addChildValue(parent.getDouble(i));
          break;
        default:
          jsonChildren.addChildValue(null);
          break;
      }
    }
    return jsonChildren;
  }

  private void prune(JsonChildren children, String currentPath, String identifier) {
    List<Object> childNodes = children.getChildren();
    List<JsonChildren.Type> childTypes = children.getChildrenTypes();
    Iterator<Object> childIterator = childNodes.iterator();
    Iterator<JsonChildren.Type> childTypesIterator = childTypes.iterator();
    int i = 0;
    for (; childIterator.hasNext() && childTypesIterator.hasNext(); ) {
      Object child = childIterator.next();
      JsonChildren.Type childType = childTypesIterator.next();
      if (childType == JsonChildren.Type.OBJECT
          && pathIsPruneField(currentPath + BEGIN_BRACKET + i + END_BRACKET, (LazyObject) child)) {
        if (debugMode) {
          LOGGER.debug(
              "Pruning {} {}{}{}{}", identifier, currentPath, BEGIN_BRACKET, i, END_BRACKET);
        }
        childIterator.remove();
        childTypesIterator.remove();
        children.decrementObjCount();
      }
      i++;
    }
  }

  private boolean pathIsPruneField(String currentPath, LazyObject node) {
    for (Map.Entry<String, String> pruneEntry : pruneFields.entrySet()) {
      String key = pruneEntry.getKey();
      String value = pruneEntry.getValue();

      String[] fields = key.split(PREDICATE_SEPARATOR);
      if (fields.length == 0 || fields.length > 2) {
        // Incorrect formatting
        return false;
      }
      if (pathEquals(currentPath, fields[0])) {
        LazyObject currentNode = node;
        String[] childNodes = fields[1].split(SEPARATOR_REGEX);
        for (int i = 0; i < childNodes.length - 1; i++) {
          if (currentNode != null && currentNode.getType() == LazyType.OBJECT) {
            currentNode = currentNode.getJSONObject(childNodes[i]);
          } else {
            return false;
          }
        }
        if (currentNode != null) {
          return getValueAsString(currentNode, childNodes[childNodes.length - 1]).equals(value);
        }
      }
    }
    return false;
  }

  private String getValueAsString(LazyObject jsonNode, String fieldName) {
    switch (jsonNode.getType(fieldName)) {
      case STRING:
        return jsonNode.getString(fieldName);
      case INTEGER:
        return Integer.toString(jsonNode.getInt(fieldName));
      case BOOLEAN:
        return Boolean.toString(jsonNode.getBoolean(fieldName));
      case FLOAT:
        return Double.toString(jsonNode.getDouble(fieldName));
      default:
        return "null";
    }
  }

  private String getValueAsString(LazyArray jsonNode, int i) {
    switch (jsonNode.getType(i)) {
      case STRING:
        return jsonNode.getString(i);
      case INTEGER:
        return Integer.toString(jsonNode.getInt(i));
      case BOOLEAN:
        return Boolean.toString(jsonNode.getBoolean(i));
      case FLOAT:
        return Double.toString(jsonNode.getDouble(i));
      default:
        return "null";
    }
  }

  private boolean pathIsIgnoreField(String currentPath) {
    if (ignoreFields != null && !ignoreFields.isEmpty()) {
      for (String ignoreField : ignoreFields) {
        if (pathEquals(currentPath, ignoreField)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean pathEquals(String path, String patternedPath) {
    String[] pathArr = path.split(SEPARATOR_REGEX);
    String[] ignorePathArr = patternedPath.split(SEPARATOR_REGEX);

    if (pathArr.length != ignorePathArr.length) {
      return false;
    }

    for (int i = 0; i < pathArr.length; i++) {
      String nodePath = pathArr[i];
      String ignoreNodeArr = ignorePathArr[i];
      boolean nodeIsArray = nodePath.endsWith(END_BRACKET);
      boolean ignoreNodeIsArray = ignoreNodeArr.endsWith(END_BRACKET);
      if (nodeIsArray && ignoreNodeIsArray) {
        String pathIndex =
            pathArr[i].substring(
                pathArr[i].lastIndexOf(BEGIN_BRACKET) + 1, pathArr[i].length() - 1);
        String ignoreIndex =
            ignorePathArr[i].substring(
                ignorePathArr[i].lastIndexOf(BEGIN_BRACKET) + 1, ignorePathArr[i].length() - 1);
        if (!pathIndex.equals(ignoreIndex) && !ignoreIndex.equals(WILDCARD)) {
          return false;
        }
        nodePath = nodePath.substring(0, nodePath.length() - pathIndex.length() - 2);
        ignoreNodeArr =
            ignoreNodeArr.substring(0, ignoreNodeArr.length() - ignoreIndex.length() - 2);
      }
      if (!nodePath.equals(ignoreNodeArr)) {
        return false;
      }
    }
    return true;
  }

  private void logInequality(String valueA, String valueB, String currentPath) {
    inequalityMessages.add(
        currentPath + " values were not the same! Expected " + valueA + " but got " + valueB);
  }

  private static String getChildPath(String currentPath, String childName) {
    return currentPath + NODE_SEPARATOR + childName;
  }

  private static boolean childIsObject(LazyObject parent, String fieldName) {
    return parent.getType(fieldName) == LazyType.OBJECT;
  }

  private static boolean childIsObject(LazyArray parent, int index) {
    return parent.getType(index) == LazyType.OBJECT;
  }

  private static boolean childIsArray(LazyObject parent, String fieldName) {
    return parent.getType(fieldName) == LazyType.ARRAY;
  }

  private static boolean childIsArray(LazyArray parent, int index) {
    return parent.getType(index) == LazyType.ARRAY;
  }

  /**
   * Sets the global debug mode for JSON comparisons. If set to true, all comparisons will be logged
   * to the console using an SLF4J implementation.
   *
   * @param debugMode the debug mode boolean value
   */
  public static void setDebugMode(boolean debugMode) {
    JsonEquals.debugMode = debugMode;
  }
}
