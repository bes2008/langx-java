package com.jn.langx.util.collection;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAttributable implements Attributable {
    protected Map<String, Object> attributes;

    public AbstractAttributable(){
        this(new HashMap<String, Object>());
    }

    public AbstractAttributable(Map<String, Object> attributes){
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
