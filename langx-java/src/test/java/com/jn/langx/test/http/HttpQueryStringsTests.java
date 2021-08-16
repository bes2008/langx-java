package com.jn.langx.test.http;

import com.jn.langx.http.HttpQueryStrings;
import com.jn.langx.util.collection.StringMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HttpQueryStringsTests {
    @Test
    public void testHttpQueryString1() {
        String url = "localhost:8888/licenses/_search?containsData=false&productName=opslinkcenter&productVersion=1.1.0&productName=bes#tag1";
        StringMap stringMap = HttpQueryStrings.getQueryStringStringMap(url);
        System.out.println(stringMap);
    }

    @Test
    public void testHttpQueryString2() throws UnsupportedEncodingException {
        String url = "localhost:8888/licenses/_search?containsData=false&productName=opslink center&productVersion=1.1.0&productName=bes#tag1";

        MultiValueMap<String, String> multiValueMap = HttpQueryStrings.getQueryStringMultiValueMap(url);

        String s = HttpQueryStrings.toQueryString(multiValueMap, true);
        System.out.println(s);

    }

    @Test
    public void testHttpQueryString3() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("hello", "1");
        map.put("hello2", 2);
        map.put("hello3", new int[]{2, 3});

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("world", true);
        map2.put("key", 2);
        map.put("hello4", map2);

        String str = HttpQueryStrings.toQueryString(map, null);
        System.out.println(str);
    }


}
