package com.jn.langx.test.cache;

import com.jn.langx.cache.*;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Dates;
import com.jn.langx.util.collection.DistinctLinkedBlockingQueue;
import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.random.ThreadLocalRandom;
import com.jn.langx.util.timing.timer.DistinctHashedWheelTimeoutFactory;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.Timer;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CacheTests3 {
    @Test
    public void test() throws Throwable {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10, 10,
                60, TimeUnit.SECONDS,
             //   new DistinctLinkedBlockingQueue<Runnable>(),
                new LinkedBlockingQueue<Runnable>(),
                new CommonThreadFactory()
        );

        final Timer timer = new HashedWheelTimer(
                Executors.defaultThreadFactory(),
                100,
                TimeUnit.MILLISECONDS,
                60,
                false,
                1000,
                threadPoolExecutor,
                DistinctHashedWheelTimeoutFactory.INSTANCE
        );
        final Cache<String, String> cache = CacheBuilder.<String, String>newBuilder()
                .cacheClass(LRUCache.class)
                .timer(timer)
                .removeListener(new RemoveListener<String, String>() {
                    public void onRemove(String key, String value, RemoveCause cause) {
                        System.out.println(StringTemplates.formatWithPlaceholder("{} deleted: key:{}, value:{}, cause:{}", Dates.nowReadableString(), key, value, cause));
                    }
                })
                .evictExpiredInterval(1000)
                .refreshAllInterval(2000)
                .distinctWhenRefresh(true)
                .loader(new AbstractCacheLoader<String, String>() {
                    @Override
                    public String load(String key) {
                        try {
                            Thread.sleep(10000L);
                            String newValue= ThreadLocalRandom.current().nextInt() + "";
                            System.out.println("Refresh Value: "+ newValue);
                            return newValue;
                        }catch (Throwable ex){
                            ex.printStackTrace();
                            return "ERROR: "+ex.getMessage();
                        }
                    }
                })
                .build();

        cache.set("hello", "cache", 60, TimeUnit.SECONDS);

        Thread.sleep(12000*1000);

    }
}
