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

import java.util.List;

/** A class used to simply hold the result of a comparison between two {@link JsonRoot}s. */
public class JsonCompareResult {

  private final boolean isEqual;
  private final List<String> successMessages;
  private final List<String> inequalityMessages;

  private JsonCompareResult(
      boolean isEqual, List<String> successMessages, List<String> inequalityMessages) {
    this.isEqual = isEqual;
    this.successMessages = successMessages;
    this.inequalityMessages = inequalityMessages;
  }

  public static JsonCompareResult of(
      boolean isEqual, List<String> successMessages, List<String> inequalityMessages) {
    return new JsonCompareResult(isEqual, successMessages, inequalityMessages);
  }

  public List<String> getSuccessMessages() {
    return successMessages;
  }

  public List<String> getInequalityMessages() {
    return inequalityMessages;
  }

  public boolean isEqual() {
    return isEqual;
  }

  public int getSuccessCount() {
    return successMessages.size();
  }

  public int getInequalityCount() {
    return inequalityMessages.size();
  }

  public int getTotalMessageCount() {
    return successMessages.size() + inequalityMessages.size();
  }
}
