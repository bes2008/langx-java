package com.jn.langx.util.reflect.parameter;

import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.Parameter;

public interface ParameterSupplier<E, O extends Parameter<E>> extends Supplier<ParameterMeta, O> {
    /**
     * java.lang.reflect.Parameter 是在JDK 1.8才出现的
     * <p>
     * 所以这个方法是判断，底层是否是用的 JDK 提供的 Parameter API
     *
     * @return true if using jdk 1.8
     */
    boolean usingJdkApi();
}
