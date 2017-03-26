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

import java.util.List;

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
