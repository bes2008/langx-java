package com.jn.langx.test.util.concurrent.promise;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
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

    private List<Promise> newPromises(Executor executor, final boolean hasError) {
        Promise promise = new Promise(executor, new Task() {
            @Override
            public Object run(Handler resolve, Handler reject) {
                return "promise";
            }
        });
        List<Promise> promises = Lists.newArrayList();
        promises.add(promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber1: " + lastResult;
            }
        }));


        promises.add(promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber2: " + lastResult;
            }
        }));


        promises.add(promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber3: " + lastResult;
            }
        }));


        promises.add(promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber4: " + lastResult;
            }
        }));


        promises.add(promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                if (hasError) {
                    throw new RuntimeException("subscriber5: " + lastResult);
                }
                return "subscriber5: " + lastResult;
            }
        }));


        promises.add(promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber6: " + lastResult;
            }
        }));

        promises.add(promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber7: " + lastResult;
            }
        }));

        promises.add(promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                return "subscriber8: " + lastResult;
            }
        }));

        return promises;
    }

    @Test
    public void test_all_without_error() throws Throwable {
        Executor executor = Executors.newFixedThreadPool(3);
        List<Promise> promises = newPromises(executor, false);

        List<String> result = Promises.all(executor, promises).await();
        System.out.println(Strings.join("\r\n", result));
    }

    @Test
    public void test_all_with_error() throws Throwable {
        Executor executor = Executors.newFixedThreadPool(3);
        List<Promise> promises = newPromises(executor, true);

        List<String> result = Promises.all(executor, promises).await();
        System.out.println(Strings.join("\r\n", result));
    }

    @Test
    public void test_any_without_error() {
        Executor executor = Executors.newFixedThreadPool(10);

        List<Promise> promises = newPromises(executor, false);
        String result = Promises.any(executor, promises).await();
        System.out.println(result);
    }

    @Test
    public void test_any_batch_without_error() {
        for (int i = 0; i < 1000; i++) {
            test_any_without_error();
        }
    }


    @Test
    public void test_any_with_error() {
        Executor executor = Executors.newFixedThreadPool(10);

        List<Promise> promises = newPromises(executor, true);
        String result = Promises.any(executor, promises).await();
        System.out.println(result);
    }


    @Test
    public void test_any_batch_with_error() {
        for (int i = 0; i < 1000; i++) {
            test_any_with_error();
        }
    }

    @Test
    public void test_all_settled() {
        Executor executor = Executors.newFixedThreadPool(10);
        List<Promise> promises = newPromises(executor, true);
        List<String> result = Promises.allSettled(executor, promises).await();
        System.out.println(Strings.join("\r\n", result));
    }
}
