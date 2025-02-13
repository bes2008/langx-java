package com.jn.langx.text.i18n;

/**
 * I18nMessageStorageAware接口用于定义设置国际化消息存储的逻辑
 * 实现该接口的类将能够接收和使用一个I18nMessageStorage对象，以便进行国际化消息的存储和检索
 */
public interface I18nMessageStorageAware {
    /**
     * 设置国际化消息存储
     *
     * @param storage I18nMessageStorage对象，用于存储和检索国际化消息
     *                实现类可以通过此方法接收一个I18nMessageStorage实例，从而获得消息存储和检索的能力
     */
    void setI18nMessageStorage(I18nMessageStorage storage);
}
