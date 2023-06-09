package com.jn.langx.io.stream.obj;

import java.io.ObjectStreamClass;

/**
 * @since 5.2.9
 */
final class DefaultSecureObjectClassPredicate implements SecureObjectClassPredicate {
    @Override
    public boolean test(ObjectStreamClass objectStreamClass) {
        return false;
    }
}
