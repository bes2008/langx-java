package com.jn.langx.el.boundary;

import com.jn.langx.util.function.Predicate;

public interface Boundary extends Predicate<String> {
    @Override
    boolean test(String value);
}
