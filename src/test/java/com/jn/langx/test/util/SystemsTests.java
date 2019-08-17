package com.jn.langx.test.util;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Collects;
import com.jn.langx.util.function.Consumer2;
import org.junit.Test;

public class SystemsTests {
    @Test
    public void testSystemProperties(){
       Collects.forEach(System.getProperties(), new Consumer2<Object, Object>() {
           @Override
           public void accept(Object key, Object value) {
               System.out.println(StringTemplates.formatWithoutIndex("{} = {}", key, value));
           }
       });

    }
}
