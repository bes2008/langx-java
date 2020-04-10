package com.jn.langx.util.reflect.reference;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public enum ReferenceType {
    /**
     * Indicates a normal Java strong reference should be used
     */
    STRONG,
    /**
     * Indicates a {@link WeakReference} should be used
     */
    WEAK,
    /**
     * Indicates a {@link SoftReference} should be used
     */
    SOFT
}
