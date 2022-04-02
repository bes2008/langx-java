package com.jn.langx.test.util.id;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.id.SnowflakeIdGenerator;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SnowflakeTests {
    @Test
    public void test() {
        System.out.println("single thread test");
        Collects.forEach(Arrs.range(100), new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(new SnowflakeIdGenerator().get());
            }
        });

    }

    @Test
    public void test2() throws Throwable {
        System.out.println("multiple thread tests");
        int threads = 10;
        final Executor executor = Executors.newFixedThreadPool(threads, new CommonThreadFactory("SnowflakeTest", false));

        final CountDownLatch countDownLatch = new CountDownLatch(threads);
        Collects.forEach(Arrs.range(threads), new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Collects.forEach(Arrs.range(100), new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) {
                                System.out.println(Thread.currentThread().getName() + "---" + new SnowflakeIdGenerator().get());
                            }
                        });

                        countDownLatch.countDown();
                    }
                });
            }
        });
        countDownLatch.await();

    }
}
