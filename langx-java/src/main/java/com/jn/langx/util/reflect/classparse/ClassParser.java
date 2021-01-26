package com.jn.langx.util.reflect.classparse;

import com.jn.langx.Parser;

public interface ClassParser<R> extends Parser<Class, R> {
    @Override
    R parse(Class clazz);
}
