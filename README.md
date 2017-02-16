# JsonEquals

JsonEquals is a simple JSON deep-equality comparator for Java.
It ignores the ordering of elements inside the JSON during comparison, and is perfect for comparing JSON responses between production and staging environments when your project is due for an API upgrade.

## Usage

JsonEquals uses [LazyJSON](https://github.com/doubledutch/LazyJSON), a simple and lightweight Java library to *parse (read)* JSON.

To compare two JSON strings,  

JsonEquals is capable of *ignoring* fields and *pruning* JSON objects within JSON arrays before comparison.
For example, responses with different timestamps can be ignored.

### Ignore Fields

Supplying a `List<String>` of 

### Pruning Objects Inside Arrays


Check out the test files for some more examples.

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