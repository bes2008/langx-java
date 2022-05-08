package com.jn.langx.util.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 4.6.2
 */
public class AttributableSet implements Attributable {
    protected Map<String, Object> attributes;

    public AttributableSet(){
        this(new HashMap<String, Object>());
    }

    public AttributableSet(Map<String, Object> attributes){
        this.attributes = attributes;
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        return this.attributes.containsKey(name);
    }

    @Override
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    @Override
    public Iterable<String> getAttributeNames() {
        return this.attributes.keySet();
    }
}
