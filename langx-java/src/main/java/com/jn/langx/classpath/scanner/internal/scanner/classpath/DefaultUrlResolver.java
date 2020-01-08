package com.jn.langx.classpath.scanner.internal.scanner.classpath;

import java.io.IOException;
import java.net.URL;

/**
 * Default implementation of UrlResolver.
 */
public class DefaultUrlResolver implements UrlResolver {
    public URL toStandardJavaUrl(URL url) throws IOException {
        return url;
    }
}
