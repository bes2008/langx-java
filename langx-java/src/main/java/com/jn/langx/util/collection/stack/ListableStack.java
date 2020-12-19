package com.jn.langx.util.collection.stack;

public class ListableStack<E> extends SimpleStack<E> {

    public E get(int index) {
        return this.list.get(index);
    }

}
