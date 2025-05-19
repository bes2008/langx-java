package com.jn.langx.util.concurrent.cancellation;

import com.jn.langx.Action;

public class CancellationToken {
    boolean isRequested;

    public CancellationToken(boolean canceled) {

    }

    public boolean isCancellationRequested() {
        return isRequested;
    }

    public void register(Action action) {

    }

}
