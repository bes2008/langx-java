package com.jn.langx.util.retry.backoff;

import com.jn.langx.util.retry.BackoffPolicy;

public class FixedBackoffPolicy extends BackoffPolicy {
    public static FixedBackoffPolicy INSTANCE = new FixedBackoffPolicy();
}