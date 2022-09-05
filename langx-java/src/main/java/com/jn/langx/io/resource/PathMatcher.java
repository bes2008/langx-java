package com.jn.langx.io.resource;

import com.jn.langx.Matcher;

public interface PathMatcher extends Matcher<String, Boolean> {
    @Override
    Boolean matches(String path);
}
