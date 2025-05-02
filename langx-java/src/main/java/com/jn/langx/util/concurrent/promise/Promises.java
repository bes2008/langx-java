package com.jn.langx.util.concurrent.promise;

import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.struct.Holder;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Promises {

    /**
     * 用于模拟 JavaScript中的async/await 中的 await 指令。
     *
     * @param promise promise对象
     * @param <R>     返回值类型
     * @return promise 运行的返回值
     */
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


    public static class ValuedException extends RuntimeException {
        private Object value;

        public ValuedException(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }

    public static class AggregateException extends RuntimeException {
        public AggregateException() {
            super();
        }

        private List<Throwable> causes = Lists.newArrayList();

        public void add(Throwable cause) {
            causes.add(cause);
        }

        public Throwable getCause(int index) {
            return causes.get(index);
        }


    }


}
