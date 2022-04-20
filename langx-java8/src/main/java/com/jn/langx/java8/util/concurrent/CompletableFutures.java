package com.jn.langx.java8.util.concurrent;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class CompletableFutures {
    public static CompletableFuture<Void> runAsync(@NonNull Runnable runnable, @Nullable Executor executor) {
        if (executor == null) {
            return CompletableFuture.runAsync(runnable);
        } else {
            return CompletableFuture.runAsync(runnable, executor);
        }
    }

    public static <T> CompletableFuture<T> supplyAsync(@NonNull Supplier<T> runnable, @Nullable Executor executor) {
        if (executor == null) {
            return CompletableFuture.<T>supplyAsync(runnable);
        } else {
            return CompletableFuture.<T>supplyAsync(runnable, executor);
        }
    }
}
