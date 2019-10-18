package com.jn.langx.test.cache;

import com.jn.langx.cache.Cache;
import com.jn.langx.cache.CacheBuilder;
import com.jn.langx.cache.LRUCache;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import org.junit.Test;

public class CacheTests {
    @Test
    public void testBasic() {
        final Cache<String, String> cache = CacheBuilder.<String, String>newBuilder()
                .cacheClass(LRUCache.class)
                .evictExpiredInterval(60 * 1000)
                .build();
        cache.set("1", "a");
        cache.set("2", "b");
        cache.set("3", "c");

        Collects.forEach(Arrs.range(10), new Consumer<Integer>() {
            @Override
            public void accept(Integer index) {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (index < 1) {
                    System.out.println(cache.get("1"));
                }
                System.out.println(cache.size());
            }
        });

    }
}
