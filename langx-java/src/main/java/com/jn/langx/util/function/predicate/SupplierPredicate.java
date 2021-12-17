package com.jn.langx.util.function.predicate;


import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier0;

public abstract class SupplierPredicate<V> implements Predicate<Supplier0<V>> {
    @Override
    public boolean test(Supplier0<V> valueSupplier){
        return doTest(valueSupplier.get());
    }

    protected abstract boolean doTest(V value);
}
