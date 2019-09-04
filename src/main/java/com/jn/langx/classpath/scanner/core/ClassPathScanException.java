package com.jn.langx.classpath.scanner.core;

/**
 * Exceptions throw when scanning the classpath.
 */
public class ClassPathScanException extends RuntimeException {

    public ClassPathScanException(String msg) {
        super(msg);
    }

    public ClassPathScanException(String msg, Exception e) {
        super(msg, e);
    }

    public ClassPathScanException(Throwable e) {
        super(e);
    }
}