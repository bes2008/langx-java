package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.MultiKeyMap;
import org.junit.Test;

public class MultKeyMapTest {
    @Test
    public void test(){
        MultiKeyMap<String> map = new MultiKeyMap<String>();
        map.put(1,2,3,"hello");
        map.put(1,2,"world");

        System.out.println(map.containsKey(1,2));
    }
}
