package com.jn.langx;

/**
 * ObjectAccessor接口定义了访问和操作对象属性或方法的通用方法。
 * 它允许外部以统一的方式对对象的属性进行设置、获取以及调用对象的方法
 */
public interface ObjectAccessor {
    /**
     * 设置对象的指定属性值
     *
     * @param property 属性名，用于标识需要设置的属性
     * @param value    要设置的属性值
     */
    void setProperty(String property, Object value);

    /**
     * 获取对象的指定属性值
     *
     * @param property 属性名，用于标识需要获取的属性
     * @return 指定属性的值，类型为Object，需要时可以进行类型转换
     */
    Object getProperty(String property);

    /**
     * 调用对象的指定方法
     *
     * @param function 方法名，用于标识需要调用的方法
     * @param args      方法参数，可变长度的参数列表，包含调用方法所需的所有参数
     * @return 方法调用的结果，类型为Object，需要时可以进行类型转换
     */
    Object invoke(String function, Object... args);
}
