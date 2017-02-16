package com.github.alphahelix00.jsonequals;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyObject;

import java.util.List;

/**
 * Created by zxiao on 2/15/17.
 */
public class JsonChildren {

    private final List<LazyArray> childArrays;
    private final List<LazyObject> childObjects;
    private final List<Object> childPrimitives;

    private JsonChildren(List<LazyObject> childObjects, List<LazyArray> childArrays, List<Object> childPrimitives) {
        this.childObjects = childObjects;
        this.childArrays = childArrays;
        this.childPrimitives = childPrimitives;
    }

    public static JsonChildren of(List<LazyObject> childObjects, List<LazyArray> childArrays, List<Object> childPrimitives) {
        return new JsonChildren(childObjects, childArrays, childPrimitives);
    }

    public List<LazyObject> getChildObjects() {
        return childObjects;
    }

    public List<LazyArray> getChildArrays() {
        return childArrays;
    }

    public List<Object> getChildPrimitives() {
        return childPrimitives;
    }

    public boolean isEmpty() {
        return childObjects.isEmpty() && childArrays.isEmpty() && childPrimitives.isEmpty();
    }

    public int objectCount() {
        return childObjects.size();
    }

    public int arrayCount() {
        return childArrays.size();
    }

    public int primitiveCount() {
        return childPrimitives.size();
    }
}
