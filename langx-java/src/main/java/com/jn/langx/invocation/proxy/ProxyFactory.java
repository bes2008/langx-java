package com.jn.langx.invocation.proxy;

import com.jn.langx.Factory;

/**
 * ProxyFactory接口继承自Factory接口，用于创建代理对象
 * 它的存在意义在于为特定的输入类型I和输出类型O提供一个代理工厂
 * 通过这个工厂，可以创建出能够代理实际对象的代理对象，常用于AOP（面向切面编程）、RPC（远程过程调用）等场景
 *
 * @param <I> 输入类型，通常是指代理对象需要包装的实际对象的类型
 * @param <O> 输出类型，即代理工厂产出的代理对象的类型，这种设计允许产出的代理对象类型与输入类型不同
 */
public interface ProxyFactory<I, O> extends Factory<I, O> {
    // 此处没有方法定义，因为ProxyFactory<I, O>继承自Factory<I, O>，它可能使用Factory<I, O>中定义的方法
    // 或者它仅仅是一个标记接口，用于区分其他类型的Factory
}
