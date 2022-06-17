package com.jn.langx.navigation;

import java.util.List;

public interface Navigator<Context> {
    <E> E get(Context context, String expression);

    <E> List<E> getList(Context context, String expression);

    <E> void set(Context context, String expression, E value);
}
