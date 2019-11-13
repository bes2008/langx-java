package com.jn.langx.test.cache;

import com.jn.langx.cache.*;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Dates;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.concurrent.TimeUnit;

public class CacheTests {

    public void testBasic() {
        final Cache<String, String> cache = CacheBuilder.<String, String>newBuilder()
                .cacheClass(LRUCache.class)
                .evictExpiredInterval(2 * 1000)
                .expireAfterRead(60)
                .expireAfterWrite(60)
                .removeListener(new RemoveListener<String, String>() {
                    @Override
                    public void onRemove(String key, String value, RemoveCause cause) {
                        System.out.println(StringTemplates.formatWithPlaceholder("{} deleted: key:{}, value:{}, cause:{}", Dates.nowReadableString(), key, value, cause));
                    }
                })
                .build();
        cache.set("1", "a", 10, TimeUnit.SECONDS);
        cache.set("2", "b", 20, TimeUnit.SECONDS);
        cache.set("3", "c", 30, TimeUnit.SECONDS);
        cache.set("4", "b", 40, TimeUnit.SECONDS);
        cache.set("5", "c", 50, TimeUnit.SECONDS);
        cache.set("6", "b", 60, TimeUnit.SECONDS);
        cache.set("7", "c", 70, TimeUnit.SECONDS);
        cache.set("8", "a", 80, TimeUnit.SECONDS);
        cache.set("9", "b", 90, TimeUnit.SECONDS);
        cache.set("10", "c", 100, TimeUnit.SECONDS);
        cache.set("11", "b", 110, TimeUnit.SECONDS);
        cache.set("12", "c", 120, TimeUnit.SECONDS);
        cache.set("13", "b", 130, TimeUnit.SECONDS);
        cache.set("14", "c", 140, TimeUnit.SECONDS);


        Collects.forEach(Arrs.range(10), new Consumer<Integer>() {
            @Override
            public void accept(Integer index) {
                try {
                    Thread.sleep(8 * 1000);
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
