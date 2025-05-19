package com.jn.langx.util.concurrent.cancellation;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Globals;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.timing.timer.Timeout;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/**
 * 这是一种协作取消模型。采用类似于事件监听机制的方式实现。
 */
public class CancellationTokenSource implements Closeable {
    private CancellationToken token;
    private static final int STATE_INIT = 0;
    private int state = STATE_INIT;

    @Nullable
    private Timeout timeout;

    public CancellationTokenSource() {
        this.token = new CancellationToken(false);
    }

    public CancellationTokenSource(int delayMills) {
        this();
        cancelAfter(delayMills);
    }

    public boolean isCanceled() {
        return token.isCancellationRequested();
    }

    public CancellationToken getToken() {
        return token;
    }

    public void cancel() {

    }

    public void cancelAfter(int delayMills) {
        Preconditions.checkArgument(delayMills >= 0);
        if (delayMills == 0) {
            cancel();
        } else {
            this.timeout = Globals.getWheelTimer().newTimeout(new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            }, delayMills, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void close() {
        if (this.timeout != null) {
            if (!this.timeout.isExpired()) {
                this.timeout.cancel();
            }
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
