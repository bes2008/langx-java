package com.jn.langx.util.concurrent;

import com.jn.langx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Atomics {
    private Atomics(){}
    public static <R> AtomicReference<R> newReference(){
        return new AtomicReference<R>();
    }

    public static <R> AtomicReference<R> newReference(@Nullable R initialValue){
        return new AtomicReference<R>(initialValue);
    }

    public static <R> AtomicReferenceArray<R> newReferenceArray(int length){
        return new AtomicReferenceArray<R>(length);
    }

    public static <R> AtomicReferenceArray<R> newReferenceArray(R[] array){
        return new AtomicReferenceArray<R>(array);
    }

}
