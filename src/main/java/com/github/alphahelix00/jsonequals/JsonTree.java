package com.github.alphahelix00.jsonequals;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyElement;
import me.doubledutch.lazyjson.LazyObject;
import me.doubledutch.lazyjson.LazyType;

import java.util.List;
import java.util.Map;

import static com.github.alphahelix00.jsonequals.Constants.BEGIN_BRACKET;
import static com.github.alphahelix00.jsonequals.Constants.BEGIN_CURLY;

/**
 * Created by zxiao on 2/15/17.
 */
public class JsonTree {

    private final LazyElement root;
    private final LazyType rootType;

    // ------------
    // Constructors
    // ------------

    private JsonTree(LazyElement root) {
        this.root = root;
        if (root instanceof LazyObject) {
            this.rootType = LazyType.OBJECT;
        } else if (root instanceof LazyArray) {
            this.rootType = LazyType.ARRAY;
        } else {
            this.rootType = LazyType.NULL;
        }
    }

    private JsonTree(String raw) {
        if (raw.startsWith(BEGIN_CURLY)) {
            this.rootType = LazyType.OBJECT;
            this.root = new LazyObject(raw);
        } else if (raw.startsWith(BEGIN_BRACKET)) {
            this.rootType = LazyType.ARRAY;
            this.root = new LazyArray(raw);
        } else {
            // This should never happen! The root of a JSON node should always be either an object or array
            this.rootType = LazyType.NULL;
            this.root = null;
        }
    }

    public static JsonTree from(LazyElement root) {
        return new JsonTree(root);
    }

    public static JsonTree from(String raw) {
        return new JsonTree(raw);
    }

    // ------------------
    // Comparator Methods
    // ------------------

    public JsonCompareResult compareTo(JsonTree other, List<String> ignoreFields, Map<String, String> pruneFields) {
        if (other != null) {
            if (this.isRootObject() && other.isRootObject()) {
                // JSON object node
                return JsonEquals.ofType(LazyType.OBJECT).withSource(this.getRoot()).withComparate(other.getRoot()).withIgnoreFields(ignoreFields).withPruneFields(pruneFields).compare();
            } else if (this.isRootArray() && other.isRootArray()) {
                // JSON array node
                return JsonEquals.ofType(LazyType.ARRAY).withSource(this.getRoot()).withComparate(other.getRoot()).withIgnoreFields(ignoreFields).withPruneFields(pruneFields).compare();
            }
        }
        return null;
    }

    public JsonCompareResult compareTo(JsonTree other) {
        return compareTo(other, null, null);
    }

    public JsonCompareResult compareToWithIgnore(JsonTree other, List<String> ignoreFields) {
        return compareTo(other, ignoreFields, null);
    }

    public JsonCompareResult compareToWithPrune(JsonTree other, Map<String, String> pruneFields) {
        return compareTo(other, null, pruneFields);
    }

    // -----------------
    // Getters / Setters
    // -----------------

    public LazyElement getRoot() {
        return this.root;
    }

    public boolean isRootObject() {
        return this.rootType == LazyType.OBJECT;
    }

    public boolean isRootArray() {
        return this.rootType == LazyType.ARRAY;
    }

    // ---------
    // Overrides
    // ---------

    @Override
    public String toString() {
        return this.root.toString();
    }
}
