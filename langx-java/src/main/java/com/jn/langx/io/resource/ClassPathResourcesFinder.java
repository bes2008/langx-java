package com.jn.langx.io.resource;

import com.jn.langx.util.collection.Lists;

import java.util.List;

class ClassPathResourcesFinder implements PathResourceFinder{
    @Override
    public List<Resource> find(ClassLoader classLoader, String pathPattern, PathMatcher pathMatcher) {
        return Lists.immutableList();
    }
}
