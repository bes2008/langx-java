package com.jn.langx.util.reflect.parameter;

import com.jn.langx.util.reflect.Parameter;

import java.lang.reflect.Constructor;

/**
 * ConstructorParameter接口继承自Parameter接口，专门用于处理构造器参数
 * 它的存在使得我们可以对构造器的参数进行特定的处理或获取参数信息
 * 例如，通过此接口，我们可以获取构造器参数的注解、参数类型、参数名等信息
 * 这在反射机制中尤其有用，当我们需要动态地了解或操作一个类的构造器参数时，可以通过ConstructorParameter接口来实现
 */
public interface ConstructorParameter extends Parameter<Constructor> {
}
