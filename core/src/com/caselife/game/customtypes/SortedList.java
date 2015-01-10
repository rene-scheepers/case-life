package com.caselife.game.customtypes;

import java.util.ArrayList;
import java.util.Collections;

public class SortedList<T extends Comparable> {

    private ArrayList<T> objects = new ArrayList();

    public T getFirst() {
        return objects.get(0);
    }

    public ArrayList<T> getObjects() {
        return objects;
    }

    public boolean contains(T object) {
        return objects.contains(object);
    }

    public void add(T object) {
        objects.add(object);
        Collections.sort(objects);
    }

    public void clear() {
        objects.clear();
    }

    public ArrayList<T> clone() {
        return (ArrayList<T>)objects.clone();
    }

    public void remove(T object) {
        objects.remove(object);
    }

    public int getSize() {
        return objects.size();
    }


}
