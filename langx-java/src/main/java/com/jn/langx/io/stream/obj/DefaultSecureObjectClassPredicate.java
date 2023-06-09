package com.jn.langx.io.stream.obj;

import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

import java.io.ObjectStreamClass;

/**
 * @since 5.2.9
 */
public final class DefaultSecureObjectClassPredicate implements SecureObjectClassPredicate {
    private Predicate<String> classNameMatcher = Functions.booleanPredicate(true);

    public DefaultSecureObjectClassPredicate() {
    }

    public DefaultSecureObjectClassPredicate(Predicate<String> matcher) {
        this.classNameMatcher = matcher;
    }


    @Override
    public boolean test(ObjectStreamClass osc) {
        return classNameMatcher.test(osc.getName());
    }
}
