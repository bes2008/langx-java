package com.jn.langx.java8.util.io;

import com.jn.langx.io.resource.PathMatcher;

import java.nio.file.Paths;


public class JdkPathMatcher implements PathMatcher {
    private java.nio.file.PathMatcher delegate;

    public JdkPathMatcher(java.nio.file.PathMatcher pathMatcher) {
        this.delegate = pathMatcher;
    }

    @Override
    public Boolean matches(String path) {
        return delegate.matches(Paths.get(path));
    }
}
