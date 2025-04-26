package com.jn.langx.test.util.net;

import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.net.http.HttpQueryStringAccessor;
import com.jn.langx.util.net.uri.component.UriComponentsBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpQueryParametersTests {
    @Test
    public void test() {
        String userKey = "JrWWWmlTA28Iu8nYkI9j0sEJmb0p33n95rTozAYBVVOAHftJwXmD/Q==";
        Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("userKey", userKey);
        String uri = UriComponentsBuilder.fromUriString("http://theIp:8080/login?userKey={userKey}")
                .uriVariables(uriVariables)
                .build()
                .encode()
                .toString();

        System.out.println("the userKey: " + userKey);
        System.out.println("the login uri: " + uri);

        HttpQueryStringAccessor accessor = new HttpQueryStringAccessor(uri, true);
        MultiValueMap<String, String> map = accessor.getMultiValueMap();
        String actualUserKey = map.getFirst("userKey");
        Assert.assertEquals(actualUserKey, userKey);
    }
}
