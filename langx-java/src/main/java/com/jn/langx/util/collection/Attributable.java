package com.jn.langx.util.collection;


public interface Attributable {
    void setAttribute(String name, Object value);

    Object getAttribute(String name);

    void removeAttribute(String name);

    Iterable<String> getAttributeNames();

}
