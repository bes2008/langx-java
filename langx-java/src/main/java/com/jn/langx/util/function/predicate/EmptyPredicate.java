package com.jn.langx.util.function.predicate;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;

public class EmptyPredicate<V> implements Predicate<V> {
    public static final EmptyPredicate IS_EMPTY_PREDICATE = new EmptyPredicate();
    public static final EmptyPredicate IS_NOT_EMPTY_PREDICATE = new EmptyPredicate(false);

    private boolean judgeEmpty = true;



    public EmptyPredicate() {
        this(true);
    }

    public EmptyPredicate(boolean judgeEmpty) {
        this.judgeEmpty = judgeEmpty;
    }

    @Override
    public boolean test(V value) {
        return Objs.isEmpty(value) == judgeEmpty;
    }
}
