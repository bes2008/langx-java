package com.jn.langx.environment;

import com.jn.langx.propertyset.PropertySet;

/**
 * CompoundEnvironment接口自5.4.6版本开始引入，继承自Environment接口。
 * 它扩展了Environment的功能，主要在于能够获取一组相关的属性(PropertySet)。
 * 该接口适用于需要从不同的数据源或配置中获取属性集合的场景。
 */
public interface CompoundEnvironment extends Environment{
    /**
     * 获取指定名称的属性集。
     *
     * @param name 属性集的名称，用于唯一标识一个属性集。
     * @return PropertySet对象，包含了一组相关的属性。
     *         如果没有找到对应的属性集，则返回null。
     */
    PropertySet getPropertySet(String name);
}
