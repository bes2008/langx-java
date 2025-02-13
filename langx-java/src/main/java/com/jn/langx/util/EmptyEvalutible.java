package com.jn.langx.util;

/**
 * 定义一个接口，用于检查对象是否为空或是否为null
 * 这个接口抽象了两个方法，用于提供空值和null值的检查机制
 */
public interface EmptyEvalutible {
    /**
     * 检查对象是否为空
     *
     * @return 如果对象为空，则返回true；否则返回false
     */
    boolean isEmpty();

    /**
     * 检查对象是否为null
     *
     * @return 如果对象为null，则返回true；否则返回false
     */
    boolean isNull();
}
