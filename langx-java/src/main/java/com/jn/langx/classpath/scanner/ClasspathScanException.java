package com.jn.langx.classpath.scanner;

/**
 * Exceptions throw when scanning the classpath.
 */
public class ClasspathScanException extends RuntimeException {

    public ClasspathScanException(String msg) {
        super(msg);
    }

    public ClasspathScanException(String msg, Exception e) {
        super(msg, e);
    }

    public ClasspathScanException(Throwable e) {
        super(e);
    }
}