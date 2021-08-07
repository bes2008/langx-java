package com.jn.langx.test.util.reflect;

import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.reflect.type.ParameterizedTypeGetter;
import org.junit.Test;

public class ParameterizedTypeTests {
    @Test
    public void test() {
        ParameterizedTypeGetter getter = new ParameterizedTypeGetter<Pipeline<Integer>>() {
        };
        System.out.println(1);
        Pipeline<Integer> a = Pipeline.of(1);

        getter = ParameterizedTypeGetter.forType(getter.getType());
        System.out.println(1);
    }
}
