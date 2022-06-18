package com.jn.langx.navigation;

import java.util.List;

/**
 * @since 4.6.10
 */
public interface Navigator<Context> {
    <E> E get(Context context, String pathExpression);

    <E> List<E> getList(Context context, String pathExpression);

    <E> void set(Context context, String pathExpression, E value);
}
