package com.jn.langx.test.util.concurrent.promise;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.concurrent.promise.AsyncCallback;
import com.jn.langx.util.concurrent.promise.Promise;
import com.jn.langx.util.concurrent.promise.Promises;
import com.jn.langx.util.concurrent.promise.Task;
import com.jn.langx.util.function.Handler;
import org.junit.Test;

import java.util.concurrent.Executors;

public class PromiseTests {
    @Test
    public void test() throws Throwable {
        Promise promise = new Promise(Executors.newFixedThreadPool(3), new Task() {
            @Override
            public Object run(Handler resolve, Handler reject) {
                return "promise";
            }
        }).then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return StringTemplates.formatWithPlaceholder("hello {}", lastResult);
            }
        }).then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return Integer.parseInt(lastResult.toString());
            }
        }).catchError(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return StringTemplates.formatWithPlaceholder("error: {}", lastResult);
            }
        }).then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return lastResult;
            }
        });
        Object result = Promises.await(promise);
        System.out.println(result);
    }
}
