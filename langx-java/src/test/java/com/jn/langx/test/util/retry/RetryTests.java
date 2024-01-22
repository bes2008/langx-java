package com.jn.langx.test.util.retry;

import com.jn.langx.util.concurrent.Executable;
import com.jn.langx.util.retry.RetryConfig;
import com.jn.langx.util.retry.Retryer;
import com.jn.langx.util.retry.backoff.FixedBackoffPolicy;
import com.jn.langx.util.struct.counter.IntegerCounter;
import com.jn.langx.util.struct.counter.SimpleIntegerCounter;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class RetryTests {
    @Test
    public void test() throws Exception{
        final IntegerCounter counter = new SimpleIntegerCounter(0);
        Callable<Object> task=new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                System.out.println("execute some statements: "+counter.increment());
                throw new Exception("mock an error");
            }
        };

        RetryConfig config = null;

        System.out.println("=================test 1: max attempts 先到达 ==================");

        config=new RetryConfig(2,60,5,3,5, TimeUnit.SECONDS, FixedBackoffPolicy.INSTANCE);
        retryWithIgnoreError(config, task);
        counter.set(0);

        System.out.println("=================test 2： max timeout 先到达 ==================");
        config=new RetryConfig(20000,60,5,3,5, TimeUnit.SECONDS, FixedBackoffPolicy.INSTANCE);
        retryWithIgnoreError(config, task);
        counter.set(0);

        System.out.println("=================test 3： 无时间限制，有次数限制 ==================");
        config=new RetryConfig(10,0,5,3,5, TimeUnit.SECONDS, FixedBackoffPolicy.INSTANCE);
        retryWithIgnoreError(config, task);
        counter.set(0);

        System.out.println("=================test 4： 有时间限制，无次数限制 ==================");
        config=new RetryConfig(-1,60,5,3,5, TimeUnit.SECONDS, FixedBackoffPolicy.INSTANCE);
        retryWithIgnoreError(config, task);
        counter.set(0);

        System.out.println("=================test 5： 无时间限制，无次数限制 ==================");
        config=new RetryConfig(-1,-1,5,3,5, TimeUnit.SECONDS, FixedBackoffPolicy.INSTANCE);
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
