package com.jn.langx.util.concurrent.cancellation;

import com.jn.langx.util.Globals;
import com.jn.langx.util.Preconditions;

import java.util.concurrent.TimeUnit;

/**
 * 这是一种协作取消模型。采用类似于事件监听机制的方式实现。
 */
public class CancellationTokenSource {

    private static final int NotCanceledState = 0; // default value of _state
    private static final int NotifyingState = 1;
    private static final int NotifyingCompleteState = 2;

    private volatile int state = NotCanceledState;

    private volatile boolean disposed = false;

    public CancellationTokenSource() {
        this.token = new CancellationToken(false);
    }

    public CancellationTokenSource(int delayMills) {
        this();
        cancelAfter(delayMills);
    }

    public boolean isCancellationRequested() {
        return state != NotCanceledState;
    }

    public CancellationToken getToken() {
        if (disposed) {

        }
        return token;
    }

    public void cancel() {
        cancel(false);
    }

    private void cancel(boolean throwIfFirstException) {
        throwIfDisposed();
        notifyCancellation(throwIfFirstException);
    }

    private void notifyCancellation(boolean throwIfFirstException) {
        if (state == NotCanceledState) {
            state = NotifyingState;
            // 调用回调函数
        }
    }

    public void cancelAfter(int delayMills) {
        Preconditions.checkArgument(delayMills >= 0);
        if (delayMills == 0) {
            cancel();
        } else {
            Globals.getWheelTimer().newTimeout(new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            }, delayMills, TimeUnit.MILLISECONDS);
        }
    }

    public void dispose() {
    }

    private void throwIfDisposed() {
        if (disposed) {
            throw new IllegalStateException("token source is disposed");
        }
    }

    /**
     * 创建一个链接的CancellationTokenSource，只要有一个依赖的CancellationToken被取消，那么新创建的CancellationTokenSource就会取消
     *
     * @param dependencyTokens 依赖的CancellationToken
     * @return 新创建一个CancellationTokenSource，观察依赖的tokens
     */
    public CancellationTokenSource createLinkedTokenSource(CancellationToken... dependencyTokens) {
        return new CancellationTokenSource();
    }

}
