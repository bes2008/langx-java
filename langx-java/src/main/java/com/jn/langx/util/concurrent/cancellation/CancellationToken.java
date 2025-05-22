package com.jn.langx.util.concurrent.cancellation;

import com.jn.langx.Action;

/**
 * 取消令牌，用来取消一个任务。
 * 在任务中，可以使用 isCancellationRequested() 方法判断是否取消了任务，也可以使用 throwIfCancellationRequested() 方法.
 */
public class CancellationToken {
    private CancellationTokenSource source;

    CancellationToken(CancellationTokenSource source) {
        this.source = source;
    }

    public boolean isCancellationRequested() {
        return source.isCancellationRequested();
    }

    public void register(Action callback) {
        if (!isCancellationRequested()) {
            this.source.register(callback);
        }
    }

    public void throwIfCancellationRequested() {
        if (isCancellationRequested()) {
            throw new OperationCanceledException();
        }
    }
}
