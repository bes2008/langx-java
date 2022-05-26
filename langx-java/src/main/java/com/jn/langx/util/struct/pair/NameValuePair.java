package com.jn.langx.util.struct.pair;

import com.jn.langx.Nameable;
import com.jn.langx.util.struct.Pair;

/**
 * @param <V> any type
 */
public class NameValuePair<V> extends Pair<String, V> implements Nameable {
    public NameValuePair() {
    }

    public NameValuePair(String name, V value) {
        super(name,value);
    }

    public String getName() {
        return getKey();
    }

    public void setName(String name) {
        setKey(name);
    }
}
