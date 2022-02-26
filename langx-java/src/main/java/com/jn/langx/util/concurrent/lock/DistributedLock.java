package com.jn.langx.util.concurrent.lock;

import com.jn.langx.distributed.locks.DLock;

import java.util.concurrent.locks.Condition;

public abstract class DistributedLock implements DLock {
    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("newCondition is not unsupported for distributed lock");
    }
}
