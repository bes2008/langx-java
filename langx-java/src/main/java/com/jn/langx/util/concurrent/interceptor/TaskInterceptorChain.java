package com.jn.langx.util.concurrent.interceptor;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.concurrent.TaskInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Interceptor chain for task execute.
 * <p>
 * Usage:
 * method1:
 * <pre>
 *  Callable task0 = new Callable(){run(){...}};
 *  WrappedCallable task00 = new WrappedCallable(task0);
 *  executor.submit(task00);
 *
 *  Runnable task1 = new Runnable(){run(){...}};
 *  WrappedRunnable task11 = new WrappedRunnable(task1);
 *  executor.submit(task11);
 * </pre>
 * <p>
 * method2:
 * <pre>
 *     using WrappedThread
 * </pre>
 * <p>
 * method3:
 * <pre>
 *     using CommonThreadFactory
 * </pre>
 *
 * @author jinuo.fang
 */
@Deprecated
public class TaskInterceptorChain implements TaskInterceptor {
    private List<TaskInterceptor> interceptors = new ArrayList<TaskInterceptor>(1);

    public TaskInterceptorChain() {
        interceptors.add(new LoggerInterceptor());
    }

    public void addInterceptor(TaskInterceptor interceptor) {
        if (interceptor != null) {
            TaskInterceptor loggerInterceptor = interceptors.remove(interceptors.size() - 1);
            interceptors.add(interceptor);
            interceptors.add(loggerInterceptor);
        }
    }

    @Override
    public void doBefore() {
        for (TaskInterceptor interceptor : interceptors) {
            interceptor.doBefore();
        }
    }

    @Override
    public void doAfter() {
        if (!interceptors.isEmpty()) {
            List<TaskInterceptor> chain = Collects.reverse(interceptors, true);
            for (TaskInterceptor interceptor : chain) {
                interceptor.doAfter();
            }
        }
    }

    @Override
    public void doError(Throwable ex) {
        if (!interceptors.isEmpty()) {
            List<TaskInterceptor> chain = Collects.reverse(interceptors, true);
            for (TaskInterceptor interceptor : chain) {
                interceptor.doError(ex);
            }
        }
    }

}
