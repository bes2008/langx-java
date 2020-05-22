package com.jn.langx.classpath.scanner.core;

import com.jn.langx.classpath.scanner.ClasspathScanner;
import com.jn.langx.classpath.scanner.ClasspathScannerFactory;

/**
 * Service implementation of ClassPathScannerFactory.
 */
public class ScannerFactory implements ClasspathScannerFactory {

    @Override
    public ClasspathScanner createScanner(ClassLoader classLoader) {
        return new Scanner(classLoader);
    }
}
