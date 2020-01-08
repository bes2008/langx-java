package com.jn.langx.util.collection;

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

    public static StringMap httpUrlParameters(String url) {
        if (url == null) {
            return EMPTY;
        }
        int paramPartStartIndex = url.indexOf("?") + 1;
        if (paramPartStartIndex == 0 || paramPartStartIndex == url.length()) {
            return EMPTY;
        }
        int paramPartEndIndex = url.indexOf("#");
        String queryString = paramPartEndIndex == -1 ? url.substring(paramPartStartIndex) : url.substring(paramPartStartIndex, paramPartEndIndex);
        return new StringMap(queryString, "=", "&");
    }
}
