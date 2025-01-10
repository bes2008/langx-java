package com.jn.langx.test.util.retry;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Dates;
import com.jn.langx.util.concurrent.Executable;
import com.jn.langx.util.retry.BackoffPolicy;
import com.jn.langx.util.retry.RetryConfig;
import com.jn.langx.util.retry.Retryer;
import com.jn.langx.util.retry.backoff.ExponentialBackoffPolicy;
import com.jn.langx.util.retry.backoff.FixedBackoffPolicy;
import com.jn.langx.util.retry.backoff.LoopBackoffPolicy;
import com.jn.langx.util.struct.counter.IntegerCounter;
import com.jn.langx.util.struct.counter.SimpleIntegerCounter;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class RetryTests {
    @Test
    public void testFixedBackoffPolicy() throws Exception{
        testWithBackoffPolicy(FixedBackoffPolicy.INSTANCE);
    }

    @Test
    public void testLoopBackoffPolicy() throws Exception{
        testWithBackoffPolicy(new LoopBackoffPolicy());
    }

    @Test
    public void testExponentialBackoffPolicy() throws Exception{
        testWithBackoffPolicy(ExponentialBackoffPolicy.INSTANCE);
    }

    public void testWithBackoffPolicy(BackoffPolicy backoffPolicy){
        final IntegerCounter counter = new SimpleIntegerCounter(0);
        Callable<Object> task=new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                System.out.println(StringTemplates.formatWithPlaceholder("{} : execute some statements: {}", Dates.format(new Date(), Dates.yyyy_MM_dd_HH_mm_ss), counter.increment()));
                throw new Exception("mock an error");
            }
        };

        RetryConfig config = null;

        System.out.println("=================test 1: max attempts 先到达 ==================");

        config=new RetryConfig(2,60,5,3,3600, TimeUnit.SECONDS, backoffPolicy);
        retryWithIgnoreError(config, task);
        counter.set(0);

        System.out.println("=================test 2： max timeout 先到达 ==================");
        config=new RetryConfig(20000,60,5,3,3600, TimeUnit.SECONDS, backoffPolicy);
        retryWithIgnoreError(config, task);
        counter.set(0);

        System.out.println("=================test 3： 无时间限制，有次数限制 ==================");
        config=new RetryConfig(10,0,5,3,3600, TimeUnit.SECONDS, backoffPolicy);
        retryWithIgnoreError(config, task);
        counter.set(0);

        System.out.println("=================test 4： 有时间限制，无次数限制 ==================");
        config=new RetryConfig(-1,60,5,3,3600, TimeUnit.SECONDS, backoffPolicy);
        retryWithIgnoreError(config, task);
        counter.set(0);

        System.out.println("=================test 5： 无时间限制，无次数限制 ==================");
        config=new RetryConfig(-1,-1,5,3,3600, TimeUnit.SECONDS, backoffPolicy);
        retryWithIgnoreError(config, task);
        counter.set(0);
    }

    void retryWithIgnoreError( RetryConfig config,Callable<Object> task ) {
        try {
            Retryer.execute(null,null, config, null, task);
        }catch (Exception exception){
            // ignore it
        }
    }
}
