package com.jn.langx.http;

import com.jn.langx.util.collection.StringMap;
import com.jn.langx.util.collection.multivalue.LinkedMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.struct.Entry;

public class HttpQueryStrings {
    public static StringMap getQueryStringStringMap(String url) {
        if (url == null) {
            return StringMap.EMPTY;
        }
        int paramPartStartIndex = url.indexOf("?") + 1;
        if (paramPartStartIndex == 0 || paramPartStartIndex == url.length()) {
            return StringMap.EMPTY;
        }
        int paramPartEndIndex = url.indexOf("#");
        String queryString = paramPartEndIndex == -1 ? url.substring(paramPartStartIndex) : url.substring(paramPartStartIndex, paramPartEndIndex);
        return new StringMap(queryString, "=", "&");
    }

    public static MultiValueMap<String,String> getQueryStringMultiValueMap(String url) {
        if (url == null) {
            return LinkedMultiValueMap.EMPTY;
        }
        int paramPartStartIndex = url.indexOf("?") + 1;
        if (paramPartStartIndex == 0 || paramPartStartIndex == url.length()) {
            return LinkedMultiValueMap.EMPTY;
        }
        int paramPartEndIndex = url.indexOf("#");
        String queryString = paramPartEndIndex == -1 ? url.substring(paramPartStartIndex) : url.substring(paramPartStartIndex, paramPartEndIndex);
        return Entry.getMultiValueMap(queryString, "=", "&");
    }
}
