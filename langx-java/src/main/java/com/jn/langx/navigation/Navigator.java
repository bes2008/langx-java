package com.jn.langx.navigation;

import java.util.List;

/**
 * @since 4.6.10
 */
public interface Navigator<Context> {
    <E> E get(Context context, String expression);

    <E> List<E> getList(Context context, String expression);

    <E> void set(Context context, String expression, E value);
}
