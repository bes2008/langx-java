package com.jn.langx.util.collection;

import com.jn.langx.util.net.http.HttpQueryStrings;

import java.util.HashMap;
import java.util.Map;

/**
 * A map with key, value are String.
 */
public class StringMap extends HashMap<String, String> {
    public static final StringMap EMPTY = new StringMap(0);

    public StringMap() {
        super();
    }

    public StringMap(int initialCapacity) {
        super(initialCapacity);
    }

    public StringMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public StringMap(Map<String, String> m) {
        super(m);
    }

    public StringMap(String src, String keyValueSpec, String entrySpec) {
        this(com.jn.langx.util.struct.Entry.getMap(src, keyValueSpec, entrySpec));
    }

    /**
     * 只适用于一个name对应一个value的场景
     */
    public static StringMap httpUrlParameters(String url) {
        return HttpQueryStrings.getQueryStringStringMap(url);
    }
}
