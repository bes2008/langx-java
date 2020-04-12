package com.jn.langx.cache;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.concurrent.CommonThreadFactory;
import com.jn.langx.util.concurrent.WrappedTasks;

import java.util.concurrent.Executor;

public class RemoveListeners {
    private RemoveListeners() {
    }

    public static <K, V> RemoveListener<K, V> async(final RemoveListener<K, V> listener, final Executor executor) {
        Preconditions.checkNotNull(listener);
        Preconditions.checkNotNull(executor);
        return new RemoveListener<K, V>() {
            @Override
            public void onRemove(final K key, final V value, final RemoveCause cause) {
                executor.execute(WrappedTasks.wrap(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRemove(key, value, cause);
                    }
                }));
            }
        };
    }

    public static <K, V> RemoveListener<K, V> async(final RemoveListener<K, V> listener) {
        Preconditions.checkNotNull(listener);
        return new RemoveListener<K, V>() {
            @Override
            public void onRemove(final K key, final V value, final RemoveCause cause) {
                CommonThreadFactory.INSTANCE.execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRemove(key, value, cause);
                    }
                });
            }
        };
    }
}
