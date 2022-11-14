package com.jn.langx.util.retry.backoff;

import com.jn.langx.util.retry.BackoffPolicy;

public class FixedBackoffPolicy extends BackoffPolicy {
    public final long millis;

    private FixedBackoffPolicy(long millis) {
        if (millis <= 0) {
            throw new IllegalArgumentException("Millis cannot be less than zero");
        }
        this.millis = millis;
    }
}
