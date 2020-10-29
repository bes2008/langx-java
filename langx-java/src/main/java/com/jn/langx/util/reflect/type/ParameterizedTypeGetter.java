package com.jn.langx.util.reflect.type;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.reflect.Reflects;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Usage:
 * <p>
 * <pre>
 *  List:
 *  ParameterizedType type0 = new ParameterizedTypeGetter<List<Person>>(){}.getType();
 *  ParameterizedType type1 = Types.getParameterizedType(List.class, Person.class);
 *  ParameterizedType type2 = new ParameterizedType(null, List.class, Person.class);
 *
 *  type0 equivalent to type1
 *  type0 equivalent to type2
 *
 * </pre>
 *
 * @param <T>
 */
public abstract class ParameterizedTypeGetter<T> {
    private Type rawType;
    private Type[] actualTypeArguments;

    protected ParameterizedTypeGetter() {
        parseSuperclassTypeParameter(getClass());
    }

    void parseSuperclassTypeParameter(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException(StringTemplates.formatWithPlaceholder("{} is not a parameterized type", Reflects.getFQNClassName(clazz)));
        }
        Type rawType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }
        this.rawType = rawType;
        actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
    }


    /**
     * 外层类型
     *
     * @return
     */
    public final Type getRawType() {
        return rawType;
    }

    public final Type getActualArgumentType(int index) {
        Preconditions.checkIndex(index, actualTypeArguments == null ? 0 : actualTypeArguments.length);
        return actualTypeArguments[index];
    }

    public final Type getType() {
        return getActualArgumentType(0);
    }

    @Override
    public String toString() {
        return rawType.toString();
    }
}
