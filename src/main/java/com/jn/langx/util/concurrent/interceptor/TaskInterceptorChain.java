package com.jn.langx.util.concurrent.interceptor;

import com.jn.langx.util.collect.Collects;
import com.jn.langx.util.concurrent.TaskInterceptor;

import java.util.LinkedList;
import java.util.List;

public class TaskInterceptorChain implements TaskInterceptor {
    private LinkedList<TaskInterceptor> interceptors = new LinkedList<TaskInterceptor>();

    public TaskInterceptorChain() {
        interceptors.add(new LoggerInterceptor());
    }

    public void addInterceptor(TaskInterceptor interceptor) {
        if (interceptor != null) {
            TaskInterceptor loggerInterceptor = interceptors.remove(interceptors.size() - 1);
            interceptors.addLast(interceptor);
            interceptors.addLast(loggerInterceptor);
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
