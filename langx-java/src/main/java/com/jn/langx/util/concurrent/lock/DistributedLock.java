package com.jn.langx.util.concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public abstract class DistributedLock implements Lock {
    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("newCondition is not unsupported for distributed lock");
    }
}
