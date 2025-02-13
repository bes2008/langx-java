package com.jn.langx.util.reflect.parameter;

import com.jn.langx.lifecycle.Initializable;

import java.lang.reflect.Constructor;

/**
 * ConstructorParameterSupplier接口扩展了ParameterSupplier接口，专门用于处理Constructor类型的参数供应，
 * 同时实现了Initializable接口，以支持初始化操作.这个接口的存在使得在需要时能够提供关于Constructor参数的信息，
 * 并进行必要的初始化操作.
 */
public interface ConstructorParameterSupplier extends ParameterSupplier<Constructor, ConstructorParameter>, Initializable {
}
