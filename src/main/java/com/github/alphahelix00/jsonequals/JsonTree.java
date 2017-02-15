package com.github.alphahelix00.jsonequals;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyElement;
import me.doubledutch.lazyjson.LazyObject;
import me.doubledutch.lazyjson.LazyType;

import static com.github.alphahelix00.jsonequals.Constants.CH_BEGIN_BRACKET;
import static com.github.alphahelix00.jsonequals.Constants.CH_BEGIN_CURLY;

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
        if (raw.charAt(0) == CH_BEGIN_CURLY) {
            this.rootType = LazyType.OBJECT;
            this.root = new LazyObject(raw);
        } else if (raw.charAt(0) == CH_BEGIN_BRACKET) {
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

    public boolean compareTo(JsonTree other) {
        if (other != null) {
            if (this.isRootObject() && other.isRootObject()) {
                // JSON object node
                JsonEquals.compareNode((LazyObject) this.getRoot(), (LazyObject) other.getRoot());
            } else if (this.isRootArray() && other.isRootArray()) {
                // JSON array node
                JsonEquals.compareNode((LazyArray) this.getRoot(), (LazyArray) other.getRoot());
            }
        }
        return false;
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
