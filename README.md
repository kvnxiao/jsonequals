# JsonEquals 

[![Release](https://jitpack.io/v/alphahelix00/jsonequals.svg)]
(https://jitpack.io/#alphahelix00/jsonequals)
[![CircleCI](https://circleci.com/gh/alphahelix00/jsonequals.svg?style=svg)](https://circleci.com/gh/alphahelix00/jsonequals)

JsonEquals is a simple JSON deep-equality comparator for Java.
It ignores the ordering of JSON keys during comparison, and is perfect for comparing JSON responses between production and staging environments when your project is due for an API upgrade.

One can also selectively define what equality consists of by providing fields to ignore and / or array indices to prune (see usage below).

## Usage

JsonEquals uses [LazyJSON](https://github.com/doubledutch/LazyJSON), a simple and lightweight Java library to *parse (read)* JSON.

To compare two JSON strings, simply create JsonRoot objects: `JsonRoot.from(jsonString)` and use `JsonRoot#compareTo(another JsonRoot)`
 
The `JsonRoot#compareTo` method returns a JsonCompareResult, which holds information regarding the comparison (isEqual boolean, success messages list, failure (inequality) messages list)

## Settings

JsonEquals is capable of *ignoring* fields and *pruning* JSON objects within JSON arrays before comparison.
For example, responses with different timestamps can be ignored (ignore list), and array objects containing JSON objects with certain field values can be ignored (pruned list with expected value to be filtered out).

### Ignoring JSON Fields

Supply a `List<String>` of strings in a dot-notated JSON path format to have JsonEquals ignore these nodes during comparison. Use `JsonRoot#compareToWithIgnore()`

```java
List<String> ignoreList = new ArrayList<>();

ignoreList.add(... see below comment block);
/*
ignoreList.add("$");    // Ignores root element and all sub-children
ignoreList.add("$[1]"); // If root element is an array, ignore the second object in the array including all its sub-children
ignoreList.add("$[*]"); // Use * as a wildcard to specify all elements in an array
ignoreList.add("$.data.timestamp");
// Ignores the root -> data -> timestamp values during comparison, e.g. the two JSONs below will be equal
{
    "data": {
        "name": "John Smith"
        "timestamp": 12345
    }
}

    versus
    
{
    "data": {
        "name": "John Smith"
        "timestamp": 23456
    }
}
*/

JsonCompareResult result = jsonRootA.compareToWithIgnore(jsonRootB, ignoreList);
```

### Pruning JSON Arrays

Supply a `Map<String, String>` in a dot-notated JSON path format to expected value in string form to act as a predicate. Any matches will be _**filtered out (read: removed)**_ and therefore ignored during comparison. Note that this _will_ shift the array indices. Use `JsonRoot#compareToWithPrune()`

Format: `Map<String, String>` -> `("arrayIndexObjectPath:fieldName", "valueToFilterInStringForm")`

e.g.
```java
    Map<String, String> pruneMap = new HashMap<>();
    pruneMap.put("$.someObject.someArray[*]:booleanName", "false"); // * is a wildcard to select all array elements
    
    JsonCompareResult result = jsonRootA.compareToWithPrune(jsonRootB, pruneMap);
    // Or combine both pruning and ignore list
    JsonCompareResult ignoreAndPruneResult = jsonRootA.compareTo(jsonRootB, ignoreList, pruneMap);
```
the JSON string:
```
{
    "someObject": {
        "someArray": [
            ... // Objects from index 0 to 5 go here
            {   // Object with index 6 in someArray
                "someString": "hello",
                "booleanName": "false"
            },
            {   // Object with index 7 in someArray
                "someString": "world",
                "booleanName": "true"
            }
            ... // Objects from index 8 and beyond go here
        ]
    }
}
```
In the above example, after pruning the JSON file before comparison, the object `$.someObject.someArray[6]` will be removed from comparison, which shifts object `$.someObject.someArray[7]` down to index 6, and so on, until everything that matches has been pruned from the array.
This can be useful when checking a list of responses from two different environments where there can be gaps in the arrays, for example, if we have an array of applications installed, we can define equality as having the same installed apps from both responses by pruning the apps that are not installed.

For a thorough example, see [`IgnoreAndPruneTest.java`](https://github.com/alphahelix00/jsonequals/blob/master/src/test/java/com/github/alphahelix00/jsonequals/tests/IgnoreAndPruneTest.java), along with [`ignore_prune_a.json`](https://github.com/alphahelix00/jsonequals/blob/master/tests/ignore_prune_a.json) and [`ignore_prune_b.json`](https://github.com/alphahelix00/jsonequals/blob/master/tests/ignore_prune_b.json)

#### Debug Mode

Debug mode can be enabled with `JsonEquals.setDebugMode(true)`, which will continuously log each leaf object or array primitive value being checked to the console.

#### See Examples

Check out the test files for examples.

## Installation

JsonEquals uses JitPack for distribution. See https://jitpack.io/#alphahelix00/jsonequals for more information.

Replace `@VERSION@` with the version number or commit hash.

#### Gradle
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
```groovy
    dependencies {
            compile kcom.github.kvnxiaoMaven
```xml
    ...
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ...
```
```xml
    ...
    <dependency>
        <groupId>com.github.alphahelix00</groupId>
     com.github.kvnxiaols</artifactId>
        <version>-SNAPSHOT</version>
    </dependency>
    ...
```

## Contributing

This was but a small project that I was working on. In it's current state, it has sufficiently covered all my needs for JSON comparison.
However, this doesn't mean that further improvements aren't necessary. 

In fact, all suggestions and improvements are welcome! I would love for others to contribute and make this project better. :)

If you are interested in helping out, please fork a copy of the repository, make your changes, and submit your pull requests!

## License

This project is licensed under GNU GPLv3
