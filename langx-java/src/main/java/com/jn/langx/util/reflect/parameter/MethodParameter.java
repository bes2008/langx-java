package com.jn.langx.util.reflect.parameter;

import com.jn.langx.util.reflect.Parameter;

import java.lang.reflect.Method;

/**
 * MethodParameter 接口定义了方法参数的通用规范
 * 它继承自 Parameter 接口，并特定于方法参数的处理
 * 此接口的存在允许在不同的上下文中对方法参数进行一致和标准化的处理
 */
public interface MethodParameter extends Parameter<Method> {
}
