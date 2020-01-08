package com.jn.langx.classpath.scanner.core;

import com.jn.langx.classpath.scanner.ClassPathScanner;
import com.jn.langx.classpath.scanner.ClassPathScannerFactory;

/**
 * Service implementation of ClassPathScannerFactory.
 */
public class ScannerFactory implements ClassPathScannerFactory {

    @Override
    public ClassPathScanner createScanner(ClassLoader classLoader) {
        return new Scanner(classLoader);
    }
}
