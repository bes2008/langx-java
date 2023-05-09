package com.jn.langx.accessor;

import com.jn.langx.util.reflect.Reflects;

/**
 * @since 4.6.10
 */
@SuppressWarnings("ALL")
public abstract class AbstractAccessorFactory<T> implements AccessorFactory<T> {

    @Override
    public boolean appliable(Class expectedClazz, Class actualClass) {
        return Reflects.isSubClassOrEquals(expectedClazz, actualClass);
    }
}
