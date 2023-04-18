package com.jn.langx.util.io.close;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.io.Closer;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;


public abstract class AbstractCloser<I> implements Closer<I> {

    @Override
    public void close(I i) {
        if (i != null) {
            try {
                doClose(i);
            } catch (Exception e) {
                Loggers.getLogger(getClass()).warn("error occur when close {}, error: {}", Reflects.getFQNClassName(i.getClass()), e.getMessage(), e);
            }
        }
    }

    protected abstract void doClose(@NonNull I i) throws Exception;


}
