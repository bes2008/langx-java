package com.jn.langx.test.util.concurrent.promise;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Strings;
import com.jn.langx.util.concurrent.promise.AsyncCallback;
import com.jn.langx.util.concurrent.promise.Promise;
import com.jn.langx.util.concurrent.promise.Promises;
import com.jn.langx.util.concurrent.promise.Task;
import com.jn.langx.util.function.Handler;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PromiseTests {
    @Test
    public void test_chain() throws Throwable {
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

    @Test
    public void test_all() throws Throwable {
        Executor executor = Executors.newFixedThreadPool(3);
        Promise promise = new Promise(executor, new Task() {
            @Override
            public Object run(Handler resolve, Handler reject) {
                return "promise";
            }
        });

        Promise subscriber1 = promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber1: " + lastResult;
            }
        });


        Promise subscriber2 = promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber2: " + lastResult;
            }
        });


        Promise subscriber3 = promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber3: " + lastResult;
            }
        });

        List<String> result = Promises.await(Promises.all(executor, subscriber1, subscriber2, subscriber3));
        System.out.println(Strings.join("\r\n", result));
    }

    @Test
    public void test_any() {
        Executor executor = Executors.newFixedThreadPool(3);
        Promise promise = new Promise(executor, new Task() {
            @Override
            public Object run(Handler resolve, Handler reject) {
                return "promise";
            }
        });

        Promise subscriber1 = promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber1: " + lastResult;
            }
        });


        Promise subscriber2 = promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber2: " + lastResult;
            }
        });


        Promise subscriber3 = promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber3: " + lastResult;
            }
        });

        String result = Promises.await(Promises.any(executor, subscriber1, subscriber2, subscriber3));
        System.out.println(result);
    }
}
