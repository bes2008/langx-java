package com.jn.langx.util.retry.backoff;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.retry.BackoffPolicy;
import com.jn.langx.util.retry.RetryConfig;

import java.util.List;

public class LoopBackoffPolicy extends BackoffPolicy {
    private List<Long> candidateBackoff;

    public LoopBackoffPolicy() {
        this(Collects.asList(50L, 100L, 200L, 1000L, 2 * 1000L, 5 * 1000L, 30 * 1000L, 60 * 1000L, 120 * 1000L));
    }

    public LoopBackoffPolicy(List<Long> candidateBackoff) {
        this.candidateBackoff = Pipeline.of(candidateBackoff)
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long value) {
                        return value != null && value.longValue() > 0;
                    }
                }).clearNulls().asList();
    }

    @Override
    protected long getBackoffTimeInternal(RetryConfig config, int attempts) {
        Preconditions.checkTrue(!this.candidateBackoff.isEmpty());
        int index = (attempts - 1) % this.candidateBackoff.size();
        long backoff = this.candidateBackoff.get(index);
        return backoff;
    }
}
