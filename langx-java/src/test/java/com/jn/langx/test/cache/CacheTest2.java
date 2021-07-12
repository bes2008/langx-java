package com.jn.langx.test.cache;

import com.jn.langx.cache.*;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Dates;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.Timer;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CacheTest2 {
    @Test
    public void test() throws Throwable {
        final Timer timer = new HashedWheelTimer(Executors.defaultThreadFactory());
        final Cache<String, String> cache = CacheBuilder.<String, String>newBuilder()
                .cacheClass(LRUCache.class)
                .timer(timer)
                .removeListener(new RemoveListener<String, String>() {
                    @Override
                    public void onRemove(String key, String value, RemoveCause cause) {
                        System.out.println(StringTemplates.formatWithPlaceholder("{} deleted: key:{}, value:{}, cause:{}", Dates.nowReadableString(), key, value, cause));
                    }
                })
                .build();

        cache.set("hello", "cache", 6, TimeUnit.SECONDS);
        int i = 0;
        while (i < 10) {
            i++;
            System.out.println(Dates.format(new Date(), Dates.HH_mm_ss) + ":" + cache.get("hello"));
            Thread.sleep(2000);
        }

    }
}
