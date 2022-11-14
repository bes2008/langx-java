package com.jn.langx.util.retry.backoff;

import com.jn.langx.util.retry.BackoffPolicy;
import com.jn.langx.util.retry.RetryConfig;

public class NoneBackoffPolicy extends BackoffPolicy {
    @Override
    protected long getBackoffTimeInternal(RetryConfig config, int attempts) {
        return -1L;
    }
}
