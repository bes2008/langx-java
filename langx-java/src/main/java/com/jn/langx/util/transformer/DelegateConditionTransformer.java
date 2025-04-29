package com.jn.langx.util.transformer;

import com.jn.langx.Transformer;
import com.jn.langx.util.function.Predicate;

public class DelegateConditionTransformer<I, O> extends ConditionTransformer<I, O> {
    private Transformer<I, O> delegate;

    @Override
    public O doTransform(I input) {
        return this.delegate.transform(input);
    }

    public DelegateConditionTransformer(Transformer<I, O> delegate) {
        this.delegate = delegate;
    }

    public DelegateConditionTransformer(Predicate<I> predicate, Transformer<I, O> delegate) {
        setPredicate(predicate);
        this.delegate = delegate;
    }
}
