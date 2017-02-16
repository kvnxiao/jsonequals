package com.github.alphahelix00.jsonequals;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zxiao on 2/15/17.
 */
public class JsonChildren {

    public enum Type {
        OBJECT, ARRAY, VALUE
    }

    private final List<Object> children;
    private final List<Type> childrenTypes;
    private int countObjects = 0;
    private int countArrays = 0;
    private int countPrimitives = 0;

    private JsonChildren() {
        this.children = new LinkedList<>();
        this.childrenTypes = new LinkedList<>();
    }

    public static JsonChildren create() {
        return new JsonChildren();
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public int size() {
        return children.size();
    }

    public int objectCount() {
        return countObjects;
    }

    public int arrayCount() {
        return countArrays;
    }

    public int primitiveCount() {
        return countPrimitives;
    }

    public Type getType(int index) {
        return childrenTypes.get(index);
    }

    public Object get(int index) {
        return children.get(index);
    }

    public LazyArray getArr(int index) {
        return (LazyArray) children.get(index);
    }

    public LazyObject getObj(int index) {
        return (LazyObject) children.get(index);
    }

    public void addChildObject(LazyObject obj) {
        children.add(obj);
        childrenTypes.add(Type.OBJECT);
        countObjects++;
    }

    public void addChildArray(LazyArray arr) {
        children.add(arr);
        childrenTypes.add(Type.ARRAY);
        countArrays++;
    }

    public void addChildPrimitive(Object obj) {
        children.add(obj);
        childrenTypes.add(Type.VALUE);
        countPrimitives++;
    }

    public List<Object> getChildren() {
        return children;
    }

    public List<Type> getChildrenTypes() {
        return childrenTypes;
    }

    public void pruneOnce() {
        countObjects--;
    }
}
