package com.jn.langx;

import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.function.Supplier0;

/**
 * IdGenerator接口用于生成标识符（ID）.
 * 它继承了Supplier0<String>和Supplier<E, String>接口，以支持两种不同的ID生成方式：
 * 一种是无参数的，另一种是基于特定实体E的.
 *
 * @param <E> 实体类型，用于生成特定于该实体的ID.
 */
public interface IdGenerator<E> extends Supplier0<String>, Supplier<E, String> {
    /**
     * 生成一个特定实体E的ID.
     * 此方法允许在生成ID时考虑实体的特定信息.
     *
     * @param e 实体对象，基于该对象生成ID.
     * @return 生成的特定于实体的ID.
     */
    String get(E e);

    /**
     * 生成一个无参数的ID.
     * 此方法用于在不考虑特定实体的情况下生成通用ID.
     *
     * @return 生成的通用ID.
     */
    String get();
}
