package com.jn.langx.util.concurrent.async;

import java.util.EventListener;

/**
 * Listens to the result of a {@link GenericFuture}.  The result of the asynchronous operation is notified once this listener
 * is added by calling {@link GenericFuture#addListener(GenericFutureListener)}.
 */
public interface GenericFutureListener<F extends GenericFuture<?>> extends EventListener {

    /**
     * Invoked when the operation associated with the {@link GenericFuture} has been completed.
     *
     * @param future  the source {@link GenericFuture} which called this callback
     */
    void operationComplete(F future) throws Exception;
}
