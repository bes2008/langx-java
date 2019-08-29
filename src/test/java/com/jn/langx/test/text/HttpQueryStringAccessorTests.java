package com.jn.langx.test.text;

import com.jn.langx.text.HttpQueryStringAccessor;
import org.junit.Test;

public class HttpQueryStringAccessorTests {
    @Test
    public void test() {
        HttpQueryStringAccessor accessor = HttpQueryStringAccessor.access("http://www.baidu.com/login?a=aaa&b=&c=3");
        System.out.println(accessor.get("a"));
        System.out.println(accessor.getInteger("c"));
        System.out.println(accessor.getString("b"));
    }


}
