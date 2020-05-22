package com.jn.langx.classpath.scanner;

/**
 * Factory for the ClassPathScanner service.
 */
public interface ClasspathScannerFactory {

    /**
     * Create a ClassPathScanner given the classLoader.
     */
    ClasspathScanner createScanner(ClassLoader classLoader);
}
