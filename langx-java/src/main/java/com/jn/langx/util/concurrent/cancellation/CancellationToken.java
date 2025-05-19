package com.jn.langx.util.concurrent.cancellation;

import com.jn.langx.Action;

/**
 * 取消令牌，用来取消一个任务。
 * 在任务中，可以使用 isCancellationRequested() 方法判断是否取消了任务，也可以使用 throwIfCancellationRequested() 方法.
 */
public class CancellationToken {
    private volatile boolean isRequested;

    public CancellationToken(boolean canceled) {
        this.isRequested = canceled;
    }

    public boolean isCancellationRequested() {
        return isRequested;
    }

    public void register(Action callback) {

    }

    public void throwIfCancellationRequested() {
        if (isRequested) {
            throw new OperationCanceledException();
        }
    }
}
