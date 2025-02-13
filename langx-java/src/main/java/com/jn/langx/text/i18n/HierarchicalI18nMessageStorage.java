package com.jn.langx.text.i18n;

/**
 * HierarchicalI18nMessageStorage接口定义了国际化的消息存储的层次结构
 * 它扩展了I18nMessageStorage接口，增加了父子关系的功能，使得消息查找可以在继承的层次结构中进行
 */
public interface HierarchicalI18nMessageStorage extends I18nMessageStorage{
    /**
     * 设置当前消息存储的父级消息存储
     * 这允许形成一个层次结构，以便在当前存储中未找到消息时，可以向上查找
     *
     * @param parent I18nMessageStorage的实例，作为当前存储的父级
     */
    void setParent(I18nMessageStorage  parent);

    /**
     * 获取当前消息存储的父级消息存储
     * 这用于在层次结构中定位父级存储，以便进行消息查找
     *
     * @return I18nMessageStorage的实例，表示当前存储的父级
     */
    I18nMessageStorage getParent();
}
