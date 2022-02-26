package com.jn.langx.test.util.net;

import com.jn.langx.util.net.http.HttpHeaders;
import org.junit.Test;

public class HttpHeadersTests {


    @Test
    public void test() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("a", "v0");
        httpHeaders.add("a1", "v1");

        System.out.println(httpHeaders.getFirstHeader("a","a1"));
        System.out.println(httpHeaders.getFirstHeader("b","a1"));
    }

}
