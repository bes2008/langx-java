package com.jn.langx.util.function.predicate;

import com.jn.langx.util.comparator.DelegatableComparator;

import java.util.Comparator;

public class ComparisonExpectValuedPredicate<V> extends ExpectValuedPredicate<V> implements DelegatableComparator<V> {
    /**
     * 判断时， 是否判断等于
     */
    private boolean inclusive = false;
    /**
     * 判定是否判断 小于
     */
    private boolean lessThan = false;

    private Comparator delegate;

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    public boolean isLessThan() {
        return lessThan;
    }

    public void setLessThan(boolean lessThan) {
        this.lessThan = lessThan;
    }

    @Override
    public boolean doTest(V actualValue) {
        int compareResult = compare(actualValue, getExpectedValue());
        if (lessThan) {
            return inclusive ? compareResult <= 0 : compareResult < 0;
        } else {
            return inclusive ? compareResult >= 0 : compareResult > 0;
        }
    }

    @Override
    public Comparator getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(Comparator delegate) {
        this.delegate = delegate;
    }

    @Override
    public int compare(V o1, V o2) {
        return delegate.compare(o1, o2);
    }


}
