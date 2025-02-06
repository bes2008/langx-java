package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.Factory;

import java.util.List;

/**
 * @since 4.6.10
 */
@SuppressWarnings("rawtypes")
/**
 * AccessorFactory接口扩展了Factory接口，用于创建或获取访问器(Accessor)对象。
 * 它允许基于类类型获取一个特定的Accessor实例，该实例可以用于访问和操作对象的属性。
 *
 * @param <T> Accessor将操作的主要类型。
 */
public interface AccessorFactory<T> extends Factory<Class<?>, Accessor<String,T>>{
    /**
     * 获取与给定类类型关联的Accessor对象。
     *
     * @param klass 需要获取Accessor的类类型。
     * @return 返回一个Accessor对象，用于访问和操作klass类型对象的属性。
     */
    @Override
    Accessor<String,T> get(Class<?> klass);

    /**
     * 获取当前工厂可以创建的Accessor类型的类列表。
     *
     * @return 返回一个包含所有可以由当前工厂创建的Accessor类型的类列表。
     */
    @SuppressWarnings("rawtypes")
    List<Class> applyTo();

    /**
     * 判断当前工厂是否适用于创建处理expectedClazz类型的Accessor。
     *
     * @param expectedClazz 期望的类类型。
     * @param actualClass 实际的类类型。
     * @return 如果当前工厂可以创建处理expectedClazz类型的Accessor，则返回true；否则返回false。
     */
    boolean appliable(Class expectedClazz, Class actualClass);
}
