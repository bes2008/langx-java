package com.jn.langx.test.util.concurrent;

import com.jn.langx.util.concurrent.completion.CompletableFuture;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.struct.Holder;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CompletableFutureTests {

    @Test
    public void test() {
        final Holder<Long> t1 = new Holder<Long>(0L);
        final Holder<Long> t2 = new Holder<Long>(0L);
        final Holder<Long> t3 = new Holder<Long>(0L);
        final Holder<Long> t4 = new Holder<Long>(0L);
        final Holder<Long> t5 = new Holder<Long>(0L);
        final Holder<Long> t6 = new Holder<Long>(0L);
        final Holder<Long> t7 = new Holder<Long>(0L);

        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(new Supplier0<String>() {
            @Override
            public String get() {
                t1.set(System.currentTimeMillis());
                String currentThreadName = Thread.currentThread().getName();
                System.out.println("currentThreadName: " + currentThreadName + ", start f1, now: " + t1.get());
                sleep(1);
                return "f1";
            }
        });
        CompletableFuture<String> f2 = f1.thenApply(new Function<String, String>() {
            @Override
            public String apply(String r) {
                t2.set(System.currentTimeMillis());
                String currentThreadName = Thread.currentThread().getName();
                System.out.println("currentThreadName: " + currentThreadName + ", start invoke f2, has get result " + r + ", now: " + t2.get());
                sleep(1);
                return "f2";
            }
        });
        CompletableFuture<String> f3 = f2.thenApply(new Function<String, String>() {
            @Override
            public String apply(String r) {
                t3.set(System.currentTimeMillis());
                String currentThreadName = Thread.currentThread().getName();
                System.out.println("currentThreadName: " + currentThreadName + ", start invoke f3, has get result " + r + ", now: " + t3.get());
                sleep(1);
                return "f3";
            }
        });

        CompletableFuture<String> f4 = f1.thenApply(new Function<String, String>() {
            @Override
            public String apply(String r) {
                t4.set(System.currentTimeMillis());
                String currentThreadName = Thread.currentThread().getName();
                System.out.println("currentThreadName: " + currentThreadName + ", start invoke f4, has get result " + r + ", now: " + t4.get());
                sleep(1);
                return "f4";
            }
        });
        CompletableFuture<String> f5 = f4.thenApply(new Function<String, String>() {
            @Override
            public String apply(String r) {
                t5.set(System.currentTimeMillis());
                String currentThreadName = Thread.currentThread().getName();
                System.out.println("currentThreadName: " + currentThreadName + ", start invoke f5, has get result " + r + ", now: " + t5.get());
                sleep(1);
                return "f5";
            }
        });


        CompletableFuture<String> f7 = f2.thenCombine(f5, new Function2<String, String, String>() {
            @Override
            public String apply(String s, String s2) {

                t7.set(System.currentTimeMillis());
                String currentThreadName = Thread.currentThread().getName();
                System.out.println("currentThreadName: " + currentThreadName + ", start invoke f7, has get result " + s + "\t" + s2 + ", now: " + t7.get());
                sleep(1);
                System.out.println(s);
                System.out.println(s2);
                return "f7";
            }
        });

        CompletableFuture<String> f6 = f5.thenApplyAsync(new Function<String, String>() {
            @Override
            public String apply(String r) {
                t6.set(System.currentTimeMillis());
                String currentThreadName = Thread.currentThread().getName();
                System.out.println("currentThreadName: " + currentThreadName + ", start invoke f6, has get result " + r + ", now: " + t6.get());
                sleep(1);
                return "f6";
            }
        });
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
