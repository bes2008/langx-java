package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.transform.TransformerFactory;

/**
 * <pre>
 * TransformerFactoryCustomizer接口继承自Customizer<TransformerFactory>接口
 * 它用于定制TransformerFactory实例的行为
 * TransformerFactory是用于创建Transformer实例的工厂，这些实例用于执行XML到Java对象的转换
 * 通过实现此接口，开发者可以对TransformerFactory进行配置，例如设置特定的属性或行为
 * </pre>
 */
public interface TransformerFactoryCustomizer extends Customizer<TransformerFactory> {
    /**
     * <pre>
     * 此方法用于定制TransformerFactory实例
     * 它允许开发者在创建TransformerFactory实例后，对其进行配置或修改
     * 例如，可以使用此方法来启用或禁用特定的功能，或者设置特定的参数
     * </pre>
     *
     * @param target TransformerFactory的实例，此实例将被定制
     */
    @Override
    void customize(TransformerFactory target);
}
