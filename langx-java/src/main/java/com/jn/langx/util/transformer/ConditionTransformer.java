package com.jn.langx.util.transformer;

import com.jn.langx.Transformer;
import com.jn.langx.util.function.Predicate;

public abstract class ConditionTransformer<I, O> implements Transformer<I, O> {
    private Predicate<I> predicate;

    public void setPredicate(Predicate<I> predicate) {
        this.predicate = predicate;
    }

    public Predicate<I> getPredicate() {
        return predicate;
    }

    @Override
    public O transform(I input) {
        if (predicate != null && predicate.test(input)) {
            return doTransform(input);
        }
        return (O) input;
    }

    public abstract O doTransform(I input);
}
