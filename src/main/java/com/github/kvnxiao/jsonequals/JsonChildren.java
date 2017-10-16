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

import java.util.LinkedList;
import java.util.List;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyObject;

/** A class which acts as a container for a collection of child JSON elements and values. */
public class JsonChildren {

  /** Enum which specifies the type of the JSON child element. */
  public enum Type {
    /** Specifies a JSON object. */
    OBJECT,
    /** Specifies a JSON array. */
    ARRAY,
    /** Specifies a value set from a JSON object or array. */
    VALUE
  }

  private final List<Object> children;
  private final List<Type> childrenTypes;
  private int countObjects = 0;
  private int countArrays = 0;
  private int countValues = 0;

  private JsonChildren() {
    this.children = new LinkedList<>();
    this.childrenTypes = new LinkedList<>();
  }

  /**
   * Creates a new JsonChildren instance.
   *
   * @return A new JsonChildren instance
   */
  public static JsonChildren create() {
    return new JsonChildren();
  }

  /**
   * Returns true true if there are no child JSON elements.
   *
   * @return true if there are no child JSON elements, false otherwise
   */
  public boolean isEmpty() {
    return children.isEmpty();
  }

  /**
   * Gets the number of JSON elements.
   *
   * @return The number of child JSON elements
   */
  public int size() {
    return children.size();
  }

  /**
   * Gets the number of JSON objects out of all the JSON elements stored.
   *
   * @return The number of JSON objects stored
   */
  public int objectCount() {
    return countObjects;
  }

  /**
   * Gets the number of JSON arrays out of all the JSON elements stored.
   *
   * @return The number of JSON arrays stored
   */
  public int arrayCount() {
    return countArrays;
  }

  /**
   * Gets the number of value types stored, out of all the JSON elements stored.
   *
   * @return The number of value types stored
   */
  public int valueCount() {
    return countValues;
  }

  /**
   * Gets the type of the child from the specified index.
   *
   * @param index The index to check
   * @return The {@link Type} of the child
   */
  public Type getType(int index) {
    return childrenTypes.get(index);
  }

  /**
   * Gets the child from the specified index.
   *
   * @param index The index to take from
   * @return The child object (Object type, may require casting for further operations)
   */
  public Object get(int index) {
    return children.get(index);
  }

  /**
   * Gets the child from the specified index as a LazyArray (JSON array).
   *
   * @param index The index to take from
   * @return The child JSON array
   */
  public LazyArray getArr(int index) {
    return (LazyArray) children.get(index);
  }

  /**
   * Gets the child from the specified index as a LazyObject (JSON object).
   *
   * @param index The index to take from
   * @return The child JSON object
   */
  public LazyObject getObj(int index) {
    return (LazyObject) children.get(index);
  }

  /**
   * Adds a child JSON object to this container.
   *
   * @param obj The JSON object to add
   */
  public void addChildObject(LazyObject obj) {
    children.add(obj);
    childrenTypes.add(Type.OBJECT);
    countObjects++;
  }

  /**
   * Adds a child JSON array to this container.
   *
   * @param arr The JSON array to add
   */
  public void addChildArray(LazyArray arr) {
    children.add(arr);
    childrenTypes.add(Type.ARRAY);
    countArrays++;
  }

  /**
   * Adds a value type to this container.
   *
   * @param obj The value to add
   */
  public void addChildValue(Object obj) {
    children.add(obj);
    childrenTypes.add(Type.VALUE);
    countValues++;
  }

  /**
   * Returns all the children from this container in another list.
   *
   * @return A list of children from this container.
   */
  public List<Object> getChildren() {
    return children;
  }

  /**
   * Returns all the child types from this container in another list.
   *
   * @return A list of {@link Type} from this container
   */
  public List<Type> getChildrenTypes() {
    return childrenTypes;
  }

  /** Decrements the JSON object counter by 1, used for pruning. */
  public void decrementObjCount() {
    countObjects--;
  }
}
