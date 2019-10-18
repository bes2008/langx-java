package com.jn.langx.test.cache;

import com.jn.langx.cache.Cache;
import com.jn.langx.cache.CacheBuilder;
import com.jn.langx.cache.LRUCache;
import org.junit.Test;

public class CacheTests {
    @Test
    public void testBasic(){
        Cache<String,String> cache = CacheBuilder.<String, String>newBuilder().cacheClass(LRUCache.class).build();
        cache.set("1","a");
        cache.set("2","b");
        cache.set("3","c");

    }
}
