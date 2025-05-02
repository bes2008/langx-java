package com.jn.langx.util.concurrent.promise;

import com.jn.langx.util.collection.Lists;

import java.util.List;

public class AggregateException extends RuntimeException {
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
