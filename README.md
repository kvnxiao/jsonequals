# JsonEquals 

[![CircleCI](https://circleci.com/gh/kvnxiao/jsonequals.svg?style=shield)](https://circleci.com/gh/kvnxiao/jsonequals)
[![Release](https://jitpack.io/v/kvnxiao/jsonequals.svg)](https://jitpack.io/#kvnxiao/jsonequals)

JsonEquals is a simple JSON deep-equality comparator for Java.
It ignores the ordering of JSON keys during comparison, and is perfect for comparing JSON responses between production and staging environments when your project is due for an API upgrade.

One can also selectively define what equality consists of by providing fields to ignore and / or array indices to prune (see usage below).

The current version of JsonEquals is used in production to compare JSON responses between different environments, which helps accelerate the upgrade process for updating API dependencies to newer versions by indicating differences in response data.

[Javadocs here](https://kvnxiao.github.io/jsonequals/)

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

Supply a `Map<String, String>` in a dot-notated JSON path format to expected value in string form to act as a predicate. Any matches will be _**filtered out (read: removed)**_ before the comparison starts, and therefore ignored during comparison. Note that this _will_ shift the array indices. Use `JsonRoot#compareToWithPrune()`

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

For a thorough example, see [`IgnoreAndPruneTest.java`](https://github.com/kvnxiao/jsonequals/blob/master/src/test/java/com/github/kvnxiao/jsonequals/tests/IgnoreAndPruneTest.java), along with [`ignore_prune_a.json`](https://github.com/kvnxiao/jsonequals/blob/master/tests/ignore_prune_a.json) and [`ignore_prune_b.json`](https://github.com/kvnxiao/jsonequals/blob/master/tests/ignore_prune_b.json)

#### Debug Mode

Debug mode can be enabled with `JsonEquals.setDebugMode(true)`, which will continuously log each leaf object or array primitive value being checked to the console.

#### See Examples

Check out the test files for examples.

## Installation

JsonEquals uses JitPack for distribution. See https://jitpack.io/#kvnxiao/jsonequals for more information.

Replace `@VERSION@` with the version number or commit hash.

#### Gradle
```gradle
    allprojects {
        repositories {
            jcenter()
            maven { url 'https://jitpack.io' }
        }
    }
```
```gradle
    dependencies {
        compile 'com.github.kvnxiao:jsonequals:1.0.1'
    }
```
#### Maven
```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
```
```xml
    <dependency>
        <groupId>com.github.kvnxiao</groupId>
        <artifactId>jsonequals</artifactId>
        <version>1.0.1</version>
    </dependency>
```

## Contributing

Have suggestions? See problems? Got new ideas or improvements? Feel free to submit an issue or pull request!

## License

This project is licensed under GNU GPLv3
