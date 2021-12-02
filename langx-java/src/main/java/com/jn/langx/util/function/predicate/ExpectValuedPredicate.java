package com.jn.langx.util.function.predicate;

public abstract class ExpectValuedPredicate<V> extends SupplierPredicate<V> {
    private V expectedValue;

    @Override
    public abstract boolean doTest(V actualValue);

    public V getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(V expectedValue) {
        this.expectedValue = expectedValue;
    }
}
