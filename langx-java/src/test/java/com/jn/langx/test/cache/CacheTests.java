package com.jn.langx.test.cache;

import com.jn.langx.cache.*;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Dates;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.concurrent.completion.CompletableFuture;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.Timer;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CacheTests {

    @Test
    public void test0() throws Throwable {
        final Timer timer = new HashedWheelTimer(Executors.defaultThreadFactory());
        CompletableFuture f1 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                testBasic(timer);
            }
        });
        CompletableFuture f2 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                testBasic(timer);
            }
        });

        CompletableFuture f3 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                testBasic(timer);
            }
        });
        CompletableFuture f4 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                testBasic(timer);
            }
        });
        CompletableFuture.allOf(f1, f2, f3, f4).thenRun(new Runnable() {
            @Override
            public void run() {
                System.out.println("great");
            }
        }).get();
    }


    private void testBasic(Timer timer) {
        final Cache<String, String> cache = CacheBuilder.<String, String>newBuilder()
                .cacheClass(LRUCache.class)
                .evictExpiredInterval(2 * 1000)
                .expireAfterRead(60)
                .expireAfterWrite(60)
                .timer(timer)
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


        Collects.forEach(Arrs.range(20), new Consumer<Integer>() {
            @Override
            public void accept(Integer index) {
                try {
                    Thread.sleep(8 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (index %2 ==1) {
                    System.out.println(cache.get("10"));
                }
                System.out.println(cache.size());
            }
        });
        System.out.println(cache);
    }
}
