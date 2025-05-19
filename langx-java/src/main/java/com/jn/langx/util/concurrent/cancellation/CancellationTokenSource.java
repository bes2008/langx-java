package com.jn.langx.util.concurrent.cancellation;

import java.io.Closeable;

/**
 * 采用类似于时间监听机制的方式实现
 */
public class CancellationTokenSource implements Closeable {
    private CancellationToken token;
    private boolean isCancellationRequested;
    

    public CancellationTokenSource() {

    }

    public CancellationTokenSource(CancellationToken... tokens) {

    }

    public CancellationToken getToken() {
        return token;
    }

    public void cancel() {

    }

    public void cancelAfter(int delayMills) {

    }

    @Override
    public void close() {

    }
}
