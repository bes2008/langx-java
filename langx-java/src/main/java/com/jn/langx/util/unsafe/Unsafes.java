package com.jn.langx.util.unsafe;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Unsafes {
    private Unsafes(){

    }
    private static UnsafeProxy loadFirst() {
        ServiceLoader<UnsafeProxy> unsafeProxies = ServiceLoader.load(UnsafeProxy.class);
        Iterator<UnsafeProxy> iterator = unsafeProxies.iterator();
        UnsafeProxy p = null;
        while (iterator.hasNext() && p == null) {
            try {
                UnsafeProxy proxy = iterator.next();
                p = proxy;
            } catch (Throwable ex) {
                // ignore it
            }
        }
        return p;
    }

    public static UnsafeProxy getUnsafe() {
        return unsafeProxy;
    }

    private static final UnsafeProxy unsafeProxy;

    static {
        unsafeProxy = loadFirst();
    }
}
