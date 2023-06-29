package com.jn.langx.security.privileged;

import com.jn.langx.util.function.Supplier0;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class CommonPrivilegedAction<R> implements PrivilegedAction<R> {
    private Supplier0<R> delegateAction;

    public CommonPrivilegedAction(Supplier0<R> delegateAction) {
        this.delegateAction = delegateAction;
    }

    @Override
    public R run() {
        return delegateAction.get();
    }

    public static <R> CommonPrivilegedAction<R> of(Supplier0<R> delegateAction) {
        return new CommonPrivilegedAction<R>(delegateAction);
    }

    public static <R> R doPrivileged(Supplier0<R> supplier) {
        if (System.getSecurityManager() != null) {
            return AccessController.<R>doPrivileged(CommonPrivilegedAction.<R>of(supplier));
        }
        return supplier.get();
    }
}
