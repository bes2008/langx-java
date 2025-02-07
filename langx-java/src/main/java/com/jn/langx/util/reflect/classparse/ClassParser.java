package com.jn.langx.util.reflect.classparse;

import com.jn.langx.Parser;

/**
 * ClassParser接口继承了Parser接口，专门用于解析Class类型的对象
 * 它定义了一个泛型接口，用于将Class对象转换为其他类型的对象
 *
 * @param <R> 解析后的返回类型
 */
public interface ClassParser<R> extends Parser<Class, R> {
    /**
     * 解析给定的Class对象，并返回解析后的结果
     *
     * @param clazz 待解析的Class对象
     * @return 解析后的对象，类型为R
     */
    @Override
    R parse(Class clazz);
}
