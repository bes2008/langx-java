package com.jn.langx.util.concurrent.promise;

import com.jn.langx.util.struct.Holder;

import java.util.concurrent.CountDownLatch;

public class Promises {
    public static <R> R await(Promise promise) {
        final CountDownLatch latch = new CountDownLatch(1);
        final Holder<Promise.StatedResult<R>> resultHolder = new Holder<Promise.StatedResult<R>>();
        promise.then(new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                resultHolder.set(new Promise.StatedResult(Promise.State.FULFILLED, lastResult));
                latch.countDown();
                return lastResult;
            }
        }, new AsyncCallback() {
            @Override
            public Object apply(Object lastResult) {
                resultHolder.set(new Promise.StatedResult(Promise.State.REJECTED, lastResult));
                latch.countDown();
                return lastResult;
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (resultHolder.get().getState() == Promise.State.FULFILLED) {
            return resultHolder.get().getResult();
        } else {
            if (resultHolder.get().getResult() instanceof Throwable) {
                throw toRuntimeException(promise);
            }
            return resultHolder.get().getResult();
        }
    }


    static RuntimeException toRuntimeException(Object rejectedResult) {
        if (rejectedResult instanceof Throwable) {
            if (rejectedResult instanceof RuntimeException) {
                return (RuntimeException) rejectedResult;
            }
            throw new RuntimeException((Throwable) rejectedResult);
        }
        return new ValuedException(rejectedResult);
    }

    static Throwable toThrowable(Object rejectedResult) {
        if (rejectedResult instanceof Throwable) {
            return (Throwable) rejectedResult;
        }
        throw new ValuedException(rejectedResult);
    }

}
