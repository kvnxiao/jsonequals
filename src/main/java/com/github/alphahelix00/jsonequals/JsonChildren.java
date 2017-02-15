package com.github.alphahelix00.jsonequals;

import me.doubledutch.lazyjson.LazyElement;

import java.util.List;

/**
 * Created by zxiao on 2/15/17.
 */
public class JsonChildren {

    private final List<LazyElement> childNodes;
    private final List<Object> childPrimitives;

    private JsonChildren(List<LazyElement> childNodes, List<Object> childPrimitives) {
        this.childNodes = childNodes;
        this.childPrimitives = childPrimitives;
    }

    public static JsonChildren of(List<LazyElement> childNodes, List<Object> childPrimitives) {
        return new JsonChildren(childNodes, childPrimitives);
    }

    public List<LazyElement> getChildNodes() {
        return childNodes;
    }

    public List<Object> getChildPrimitives() {
        return childPrimitives;
    }

    public boolean isEmpty() {
        return childNodes.isEmpty() && childPrimitives.isEmpty();
    }

    public int nodeCount() {
        return childNodes.size();
    }

    public int primitiveCount() {
        return childPrimitives.size();
    }
}
