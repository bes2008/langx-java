package com.jn.langx.management;

public class MBeanException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MBeanException(final String message, final Exception e) {
        super(message, e);
    }

    public MBeanException(final Exception e) {
        super(e);
    }

    public MBeanException(final String message) {
        super(message, new Exception());
    }

    public MBeanException(final Throwable e) {
        super(e);
    }
}
