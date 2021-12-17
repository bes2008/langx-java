package com.jn.langx.util.function.predicate;

import com.jn.langx.util.Objs;

public class EqualsExpectValuedPredicate<V> extends ExpectValuedPredicate<V> {
    @Override
    protected boolean doTest(V actualValue) {
        return Objs.equals(actualValue, getExpectedValue());
    }
}
