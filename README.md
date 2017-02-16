# JsonEquals

JsonEquals is a simple JSON deep-equality comparator for Java.
It ignores the ordering of JSON keys during comparison, and is perfect for comparing JSON responses between production and staging environments when your project is due for an API upgrade.

## Usage

JsonEquals uses [LazyJSON](https://github.com/doubledutch/LazyJSON), a simple and lightweight Java library to *parse (read)* JSON.

To compare two JSON strings, simply create JsonRoot objects: `JsonRoot.from(jsonString)` and use `JsonRoot#compareTo(another JsonRoot)`
 
The `JsonRoot#compareTo` method returns a JsonCompareResult, which holds information regarding the comparison (isEqual boolean, success messages list, failure (inequality) messages list)

## Settings

JsonEquals is capable of *ignoring* fields and *pruning* JSON objects within JSON arrays before comparison.
For example, responses with different timestamps can be ignored (ignore list), and array objects containing JSON objects with certain field values can be ignored (pruned list with expected value to be filtered out).

#### Ignore Fields

Supply a `List<String>` of strings in a dot-notated JSON path format to have JsonEquals ignore these nodes during comparison. Use `JsonRoot#compareToWithIgnore()`
```java
List<String> ignoreList = new ArrayList<>();

ignoreList.add(... see below comment block);
/*
ignoreList.add("$");    // Ignores root element and all sub-children
ignoreList.add("$[1]"); // If root element is an array, ignore the second object in the array including all its sub-children

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

#### Pruning Objects Inside Arrays

Supply a `Map<String, String>` of strings in a dot-notated JSON path format, with the expected output value. Any matches will be _**filtered out (read: removed)**_ and therefore ignored during comparison. Note that this _will_ shift the array indices. Use `JsonRoot#compareToWithPrune()`

Example: see [`IgnoreAndPruneTest.java`](https://github.com/alphahelix00/jsonequals/blob/master/src/test/java/IgnoreAndPruneTest.java), along with [`ignore_prune_a.json`](https://github.com/alphahelix00/jsonequals/blob/master/tests/ignore_prune_a.json) and [`ignore_prune_b.json`](https://github.com/alphahelix00/jsonequals/blob/master/tests/ignore_prune_b.json)

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
            compile 'com.github.alphahelix00:jsonequals:@VERSION@'
    }
```

#### Maven
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
        <artifactId>jsonequals</artifactId>
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