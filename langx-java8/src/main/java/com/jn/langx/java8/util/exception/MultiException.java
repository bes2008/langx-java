package com.jn.langx.java8.util.exception;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wraps multiple exceptions.
 * <p>
 * Allows multiple exceptions to be thrown as a single exception.
 * <p>
 * The MultiException itself should not be thrown instead one of the
 * ifExceptionThrow* methods should be called instead.
 */
@SuppressWarnings("serial")
public class MultiException extends Exception {
    private static final String DEFAULT_MESSAGE = "Multiple exceptions";

    private List<Throwable> nested;

    public MultiException() {
        // Avoid filling in stack trace information.
        super(DEFAULT_MESSAGE, null, false, false);
        this.nested = new ArrayList<>();
    }

    /**
     * Create a MultiException which may be thrown.
     *
     * @param nested The nested exceptions which will be suppressed by this
     *               exception.
     */
    private MultiException(List<Throwable> nested) {
        super(DEFAULT_MESSAGE);
        this.nested = new ArrayList<>(nested);

        if (!nested.isEmpty()) {
            initCause(nested.get(0));
        }
        for (Throwable t : nested) {
            if (t != this && t != getCause())
                addSuppressed(t);
        }
    }

    public void add(Throwable e) {
        if (e instanceof MultiException) {
            MultiException me = (MultiException) e;
            nested.addAll(me.nested);
        } else
            nested.add(e);
    }

    public int size() {
        return (nested == null) ? 0 : nested.size();
    }

    public List<Throwable> getThrowables() {
        if (nested == null)
            return Collections.emptyList();
        return nested;
    }

    public Throwable getThrowable(int i) {
        return nested.get(i);
    }

    /**
     * Throw a multiexception.
     * If this multi exception is empty then no action is taken. If it
     * contains a single exception that is thrown, otherwise the this
     * multi exception is thrown.
     *
     * @throws Exception the Error or Exception if nested is 1, or the MultiException itself if nested is more than 1.
     */
    public void ifExceptionThrow()
            throws Exception {
        if (nested == null)
            return;

        switch (nested.size()) {
            case 0:
                break;
            case 1:
                Throwable th = nested.get(0);
                if (th instanceof Error)
                    throw (Error) th;
                if (th instanceof Exception)
                    throw (Exception) th;
                throw new MultiException(nested);
            default:
                throw new MultiException(nested);
        }
    }

    /**
     * Throw a Runtime exception.
     * If this multi exception is empty then no action is taken. If it
     * contains a single error or runtime exception that is thrown, otherwise the this
     * multi exception is thrown, wrapped in a runtime exception.
     *
     * @throws Error            If this exception contains exactly 1 {@link Error}
     * @throws RuntimeException If this exception contains 1 {@link Throwable} but it is not an error,
     *                          or it contains more than 1 {@link Throwable} of any type.
     */
    public void ifExceptionThrowRuntime()
            throws Error {
        if (nested == null)
            return;

        switch (nested.size()) {
            case 0:
                break;
            case 1:
                Throwable th = nested.get(0);
                if (th instanceof Error)
                    throw (Error) th;
                else if (th instanceof RuntimeException)
                    throw (RuntimeException) th;
                else
                    throw new RuntimeException(th);
            default:
                throw new RuntimeException(new MultiException(nested));
        }
    }

    /**
     * Throw a multiexception.
     * If this multi exception is empty then no action is taken. If it
     * contains a any exceptions then this
     * multi exception is thrown.
     *
     * @throws MultiException the multiexception if there are nested exception
     */
    public void ifExceptionThrowMulti()
            throws MultiException {
        if (nested == null)
            return;

        if (!nested.isEmpty()) {
            throw new MultiException(nested);
        }
    }

    /**
     * Throw an Exception, potentially with suppress.
     * If this multi exception is empty then no action is taken. If the first
     * exception added is an Error or Exception, then it is throw with
     * any additional exceptions added as suppressed. Otherwise a MultiException
     * is thrown, with all exceptions added as suppressed.
     *
     * @throws Exception the Error or Exception if at least one is added.
     */
    public void ifExceptionThrowSuppressed()
            throws Exception {
        if (nested == null || nested.isEmpty())
            return;

        Throwable th = nested.get(0);
        if (!(th instanceof Error) && !(th instanceof Exception))
            th = new MultiException(Collections.emptyList());

        for (Throwable s : nested) {
            if (s != th) {
                th.addSuppressed(s);
            }
        }
        if (th instanceof Error) {
            throw (Error) th;
        }
        throw (Exception) th;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(MultiException.class.getSimpleName());
        if ((nested == null) || (nested.isEmpty())) {
            str.append("[]");
        } else {
            str.append(nested);
        }
        return str.toString();
    }
}
