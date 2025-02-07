package com.jn.langx.util.reflect.parameter;

import com.jn.langx.lifecycle.Initializable;

import java.lang.reflect.Method;

/**
 * MethodParameterSupplier接口扩展了ParameterSupplier接口，专门用于处理方法参数的供应，
 * 并且实现了Initializable接口，以支持初始化操作。实现这个接口的类应该提供方法参数的
 * 供应功能，能够在反射操作时提供详细的方法参数信息。
 */
public interface MethodParameterSupplier extends ParameterSupplier<Method, MethodParameter>, Initializable {
}
