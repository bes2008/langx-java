package com.jn.langx.text.xml.cutomizer;

import com.jn.langx.Customizer;

import javax.xml.stream.XMLInputFactory;

/**
 * XmlInputFactoryCustomizer接口继承自Customizer<XMLInputFactory>，用于定制XML输入工厂的配置
 * 它定义了一个方法，允许实现者在创建或配置XML输入工厂实例时应用自定义设置
 */
public interface XmlInputFactoryCustomizer extends Customizer<XMLInputFactory> {
    /**
     * 自定义XML输入工厂的配置
     *
     * @param target 待定制的XML输入工厂实例
     *               通过此参数，实现者可以访问和修改XML输入工厂的配置
     */
    @Override
    void customize(XMLInputFactory target);
}
