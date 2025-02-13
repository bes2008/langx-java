package com.jn.langx.pipeline;

import com.jn.langx.annotation.Prototype;
import com.jn.langx.util.struct.Holder;
@Prototype
public interface Pipeline<T> {
    /**
     * Adds a handler to the beginning of the pipeline.
     *
     * @param handler the handler to be added
     */
    void addFirst(Handler handler);

    /**
     * Adds a handler to the end of the pipeline.
     *
     * @param handler the handler to be added
     */
    void addLast(Handler handler);

    /**
     * Retrieves the head context of the pipeline.
     *
     * @return the head context
     */
    HeadHandlerContext getHead();

    /**
     * Clears all handlers from the pipeline.
     */
    void clear();

    /**
     * Clears all handlers from the pipeline except for the head and tail.
     */
    void reset();

    /**
     * Binds the target object to the pipeline.
     *
     * @param target the target object to be bound
     */
    void bindTarget(T target);

    /**
     * Unbinds the target object from the pipeline.
     */
    void unbindTarget();

    /**
     * Retrieves the target object bound to the pipeline.
     *
     * @return the target object
     */
    T getTarget();

    /**
     * Retrieves the target holder of the pipeline.
     *
     * @return the target holder
     */
    Holder<T> getTargetHolder();

    /**
     * Performs inbound operations.
     *
     * @throws Throwable if an exception occurs during the operation
     */
    void inbound() throws Throwable;

    /**
     * Performs inbound operations with a message.
     *
     * @param message the message for inbound operation
     * @throws Throwable if an exception occurs during the operation
     */
    void inbound(T message) throws Throwable;

    /**
     * Performs outbound operations.
     *
     * @throws Throwable if an exception occurs during the operation
     */
    void outbound() throws Throwable;

    /**
     * Checks if outbound operations have been performed.
     *
     * @return true if outbound operations have been performed; otherwise, false
     */
    boolean hadOutbound();

    /**
     * Retrieves the current handler context.
     *
     * @return the current handler context
     */
    HandlerContext getCurrentHandlerContext();

    /**
     * Sets the current handler context.
     *
     * @param handlerContext the handler context to be set
     */
    void setCurrentHandlerContext(HandlerContext handlerContext);
}
