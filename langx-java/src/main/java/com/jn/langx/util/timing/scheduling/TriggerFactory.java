package com.jn.langx.util.timing.scheduling;

import com.jn.langx.Factory;
import com.jn.langx.Named;
/**
 * TriggerFactory接口是一个特定的工厂接口，用于创建Trigger对象。它扩展了Factory和Named接口，
 * 继承了它们的功能。此接口的实现应该提供一种基于字符串表达式创建Trigger对象的机制。
 *
 * @since 4.6.7 表示该接口自版本4.6.7开始引入。
 */
public interface TriggerFactory extends Factory<String, Trigger>, Named {
    /**
     * 获取工厂的名称。这个方法覆盖了Named接口中的getName方法。
     *
     * @return 返回工厂的名称，以便在多个工厂存在时进行区分。
     */
    @Override
    String getName();

    /**
     * 根据给定的字符串表达式获取或创建一个Trigger对象。这个方法覆盖了Factory接口中的get方法，
     * 专门针对Trigger对象的创建。
     *
     * @param expression 是用于创建Trigger对象的字符串表达式，通常包含触发条件的信息。
     * @return 返回根据表达式创建的Trigger对象。
     */
    @Override
    Trigger get(String expression);
}
