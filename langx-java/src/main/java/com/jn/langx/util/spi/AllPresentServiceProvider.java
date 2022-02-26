package com.jn.langx.util.spi;

import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.annotation.OnClassesConditions;


public class AllPresentServiceProvider<T> extends CommonServiceProvider<T> {
    private boolean defaultValueIfMissOnClassesAnnotation;

    public AllPresentServiceProvider() {
        this(true);
    }

    public AllPresentServiceProvider(boolean defaultValueIfMissOnClassesAnnotation) {
        this.defaultValueIfMissOnClassesAnnotation = defaultValueIfMissOnClassesAnnotation;
        this.setPredicate(new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return OnClassesConditions.allPresent(t.getClass(), AllPresentServiceProvider.this.defaultValueIfMissOnClassesAnnotation);
            }
        });
    }

}
