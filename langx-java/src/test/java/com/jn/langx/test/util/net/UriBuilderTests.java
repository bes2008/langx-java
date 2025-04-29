package com.jn.langx.test.util.net;

import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.net.uri.component.UriComponentsBuilder;
import org.junit.Test;

import java.net.URI;
import java.util.Map;

public class UriBuilderTests {

    @Test
    public void test() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("service", "http://localhost:8084/portalui/#index");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8080/cas/lo gin?service={service}")
                .enableEncode()
                .uriVariables(variables);
        URI uri = builder.build().encode().toUri();
        System.out.println(uri);
    }

}
