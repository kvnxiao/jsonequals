package com.github.alphahelix00.jsonequals;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyElement;
import me.doubledutch.lazyjson.LazyObject;
import me.doubledutch.lazyjson.LazyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by zxiao on 2/15/17.
 */
public class JsonEquals {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonEquals.class);

    public static boolean compareNode(LazyObject a, LazyObject b) {
        compareNode(a, b, "$");
        return false;
    }

    public static boolean compareNode(LazyArray a, LazyArray b) {
        compareNode(a, b, "$");
        return false;
    }

    // Compare objects
    public static void compareNode(LazyObject a, LazyObject b, String currentPath) {
        System.out.println(currentPath);
        Set<String> fieldsA = a.keySet();
        Set<String> fieldsB = b.keySet();
        // TODO: filter fields set for ignoreValues
        if (fieldsA.equals(fieldsB)) {
            for (String fieldName : fieldsA) {
                if (childIsObject(a, fieldName) && childIsObject(b, fieldName)) {
                    compareNode(a.getJSONObject(fieldName), b.getJSONObject(fieldName), getChildPath(currentPath, fieldName));
                } else if (childIsArray(a, fieldName) && childIsArray(b, fieldName)) {
                    compareNode(a.getJSONArray(fieldName), b.getJSONArray(fieldName), getChildPath(currentPath, fieldName));
                } else {
                    compareValues(a, b, fieldName, getChildPath(currentPath, fieldName));
                }
            }
        } else {
            LOGGER.warn("a and b JSON objects do not have the same child key names.");
        }
    }

    // Compare arrays
    public static void compareNode(LazyArray a, LazyArray b, String currentPath) {
        System.out.println(currentPath);
        List<LazyElement> elementsA = getChildList(a);
        List<LazyElement> elementsB = getChildList(b);
        if (!elementsA.isEmpty() && !elementsB.isEmpty()) {
            // TODO: prune and filter list of children for ignores
            if (elementsA.size() == elementsB.size()) {
                for (int i = 0; i < elementsA.size(); i++) {
                    if (elementsA.get(i) instanceof LazyObject) {
                        compareNode((LazyObject) elementsA.get(i), (LazyObject) elementsB.get(i), currentPath + "[" + i  + "]");
                    } else if (elementsA.get(i) instanceof  LazyArray) {
                        compareNode((LazyArray) elementsA.get(i), (LazyArray) elementsB.get(i), currentPath + "[" + i  + "]");
                    }
                }
            } else {
                LOGGER.warn("{} JSON array not equal in length!", currentPath);
            }
        }
    }

    public static void compareValues(LazyObject a, LazyObject b, String fieldName, String currentPath) {
        System.out.println(currentPath);
        if (a.getType(fieldName) == LazyType.STRING && b.getType(fieldName) == LazyType.STRING) {

        } else if (a.getType(fieldName) == LazyType.INTEGER && b.getType(fieldName) == LazyType.INTEGER) {

        } else if (a.getType(fieldName) == LazyType.BOOLEAN && b.getType(fieldName) == LazyType.BOOLEAN) {

        } else if (a.getType(fieldName) == LazyType.FLOAT && b.getType(fieldName) == LazyType.FLOAT) {

        } else if (a.getType(fieldName) == LazyType.NULL && b.getType(fieldName) == LazyType.NULL) {

        } else {
            LOGGER.warn("{} are not of the same type!", currentPath + "." + fieldName);
        }
    }

    private static List<LazyElement> getChildList(LazyArray parent) {
        List<LazyElement> children = new LinkedList<>();
        for (int i = 0; i < parent.length(); i++) {
            if (childIsObject(parent, i)) {
                children.add(parent.getJSONObject(i));
            } else if (childIsArray(parent, i)) {
                children.add(parent.getJSONArray(i));
            } else {
                LOGGER.error("Element inside array is neither a JSON object or JSON array!");
            }
        }
        return children;
    }

    private static String getChildPath(String currentPath, String childName) {
        return currentPath + "." + childName;
    }

    private static boolean childIsObject(LazyObject parent, String fieldName) {
        return parent.getType(fieldName) == LazyType.OBJECT;
    }

    private static boolean childIsObject(LazyArray parent, int index) {
        return parent.getType(index) == LazyType.OBJECT;
    }

    private static boolean childIsArray(LazyObject parent, String fieldName) {
        return parent.getType(fieldName) == LazyType.ARRAY;
    }

    private static boolean childIsArray(LazyArray parent, int index) {
        return parent.getType(index) == LazyType.ARRAY;
    }

}
