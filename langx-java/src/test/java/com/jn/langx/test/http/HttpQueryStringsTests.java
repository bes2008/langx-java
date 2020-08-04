package com.jn.langx.test.http;

import com.jn.langx.http.HttpQueryStrings;
import com.jn.langx.util.collection.StringMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import org.junit.Test;

public class HttpQueryStringsTests {
    @Test
    public void testHttpQueryString1() {
        String url = "localhost:8888/licenses/_search?containsData=false&productName=opslinkcenter&productVersion=1.1.0&productName=bes#tag1";
        StringMap stringMap = HttpQueryStrings.getQueryStringStringMap(url);
        System.out.println(stringMap);
    }

    @Test
    public void testHttpQueryString2() {
        String url = "localhost:8888/licenses/_search?containsData=false&productName=opslinkcenter&productVersion=1.1.0&productName=bes#tag1";
        MultiValueMap multiValueMap = HttpQueryStrings.getQueryStringMultiValueMap(url);
        System.out.println(multiValueMap);
    }
}
