package com.jn.langx.io.stream.obj;

import com.jn.langx.util.function.Predicate;

import java.io.ObjectStreamClass;

/**
 * @since 5.2.9
 */
public interface SecureObjectClassPredicate extends Predicate<ObjectStreamClass> {
    @Override
    public boolean test(ObjectStreamClass objectStreamClass) ;
}
